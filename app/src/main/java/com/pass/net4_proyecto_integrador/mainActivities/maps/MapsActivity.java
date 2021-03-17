package com.pass.net4_proyecto_integrador.mainActivities.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.CollectEventData;
import com.pass.net4_proyecto_integrador.CollectUserData;
import com.pass.net4_proyecto_integrador.Evento;
import com.pass.net4_proyecto_integrador.LoginActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.User;
import com.pass.net4_proyecto_integrador.extraActivities.EventActivity;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.notifications.ComunityActivity;

import com.pass.net4_proyecto_integrador.mainActivities.profile.ProfileActivity;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CollectUserData.Comunicacion, CollectEventData.ComunicacionE {
    //probar android:launchMode="singleTask"

    //Barra de abajo
    private BottomNavigationView bnv;
    //Para el Mapa
    private GoogleMap mMap;
    private final OnMapReadyCallback omrc = this;
    private LocationManager lm;
    private static final int REQUEST_CODE = 101;
    private FirebaseUser u;
    private FloatingActionButton find_location;
    //context
    private Context contexto = this;
    private CollectUserData.Comunicacion collectUser = this;
    private CollectEventData.ComunicacionE collectEvent = this;
    User uBueno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Para llamar al metodo onMapReady
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(omrc);

        u = FirebaseAuth.getInstance().getCurrentUser();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkPermissionClient();

        find_location = findViewById(R.id.fab);
        find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerLocalizacion();
            }
        });

        CollectUserData.takeData(collectUser);

        bnv = findViewById(R.id.nav_view_maps);
        bnv.setSelectedItemId(R.id.navigation_maps);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_maps:
                        return true;
                    case R.id.navigation_activity:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_help_alert:
                        startActivity(new Intent(getApplicationContext(), HelpAlertActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_community:
                        startActivity(new Intent(getApplicationContext(), ComunityActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void checkPermissionClient() {
        //Aqui prguntaremos si quiere dar permiso de ubicacion a la aplicacion en caso de que no tenga ira al metodo onRequestPermissionsResult(),
        //si ya tiene los permiso ira directamente a obtenerLocalizacion()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //comprobamos que le dan permiso a la apliocacion para que coja la ubicacio
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obtenerLocalizacion();
            }
        }
    }

    private void obtenerLocalizacion() {
        //Aqui revisamos si estan los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Este toast es para que muestre el mensaje de que busca una ubicacion
        Toast toast = Toast.makeText(contexto, "Buscando Ubicacion.....", Toast.LENGTH_SHORT);
        toast.show();
        //Aqui ponemos un oyesnte a la ubicacion y en caso de que sea nulo le decimos que ponga la ultima encontrada
        //y si no que compruebe si la latitud y longitud no son nulas
        //en caso de que sean busca otra vez y si las encuentra que lo marque en el mapa
        LocationListener oyente_localizaciones = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                findLocation(location);
                lm.removeUpdates(this);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Toast.makeText(MapsActivity.this, "Activa el GPS", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, oyente_localizaciones);
    }

    private void findLocation(Location location) {
        if (location != null) {
            double latitud = location.getLatitude();
            double longitud = location.getLongitude();
            String username = u.getUid();
            CollectUserData.updateLocation(latitud, longitud, username);
            CollectUserData.takeData(collectUser);
        }
    }

    private void drawLocation(double latitud, double longitud, String nombre,String userID) {
        //Aqui se pasan las coordenadas
        LatLng ubi = new LatLng(latitud, longitud);
        //Aqui dirigimos la camara a la ubicacion
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubi, 13));
        //Esto es el marcador con el titulo de ubicacion
        mMap.addMarker(new MarkerOptions().position(ubi).title(nombre).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    private String changeName() {
        String nombre_user = u.getUid();
        String username;
        String[] nombreCompleto = nombre_user.split(" ");
        if (nombreCompleto.length >= 2) {
            username = nombreCompleto[0] + " " + nombreCompleto[1];
        } else {
            username = nombreCompleto[0];
        }
        return username;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void sendData(List<User> users) {
        uBueno = new User();
        mMap.clear();
        for (User user : users) {
            if (user.getUserId().equals(LoginActivity.USERUID)){
                uBueno = user;
                CollectEventData.takeData(collectEvent);
            }
        }

    }

    @Override
    public void sendDataEvento(List<Evento> listaEvento) {
        for (Evento e : listaEvento){
            drawLocationEvents(e.getLatitud(), e.getLongitud(), e.getGradoUrgencia(), e.getTitulo(), e.getUserId());
        }

        drawLocation(uBueno.getLatitud(),uBueno.getLongitud(),uBueno.getUsername(),uBueno.getUserId());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals(uBueno.getUsername())){
                    return false;
                }
                AlertDialog builder = new AlertDialog.Builder(MapsActivity.this).create();
                LayoutInflater factory = LayoutInflater.from(getApplicationContext());
                View view = factory.inflate(R.layout.activity_map_event_information,
                        null);

                ImageView imgInfo = view.findViewById(R.id.img_info);
                TextView titInfo = view.findViewById(R.id.tituloEventoInfo);
                TextView usernameInfo = view.findViewById(R.id.usuarioInfo);
                Button btn = view.findViewById(R.id.btn_info_Event);

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Events").child(marker.getTitle());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Evento e = snapshot.getValue(Evento.class);
                        Glide.with(getApplicationContext())
                                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/eventspics%2F" + marker.getTitle() + ".jpg?alt=media&token=26419bcf-488c-4c50-802a-8088e2c092b1"))
                                .placeholder(R.drawable.user_icon)
                                .centerCrop()
                                //.transition(DrawableTransitionOptions.withCrossFade(300))
                                //.circleCrop()
                                .into(imgInfo);
                        titInfo.setText(e.getTitulo());
                        usernameInfo.setText(uBueno.getUsername());

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), EventActivity.class);
                                intent.putExtra("eventUserId", e.getUserId());
                                intent.putExtra("eventTitulo", e.getTitulo());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                v.getContext().startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                builder.setView(view);

                builder.show();
                return true;
            }
        });
    }

    private void drawLocationEvents(double latitud, double longitud, int gradoUrgencia, String titulo, String userId) {
        //Aqui se pasan las coordenadas
        LatLng ubi = new LatLng(latitud, longitud);
        //Aqui dirigimos la camara a la ubicacion
        mMap.animateCamera(CameraUpdateFactory.newLatLng(ubi));
        //Esto es el marcador con el titulo de ubicacion

        if (gradoUrgencia == 0) {
            mMap.addMarker(new MarkerOptions().position(ubi)
                    .title(userId+"-"+titulo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        } else if (gradoUrgencia == 1) {
            mMap.addMarker(new MarkerOptions().position(ubi)
                    .title(userId+"-"+titulo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        } else if (gradoUrgencia == 2) {
            mMap.addMarker(new MarkerOptions().position(ubi)
                    .title(userId+"-"+titulo)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

    }
}
package com.pass.net4_proyecto_integrador.mainActivities.maps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pass.net4_proyecto_integrador.CollectUserData;
import com.pass.net4_proyecto_integrador.LoginActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.User;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.notifications.NotificationsFragment;
import com.pass.net4_proyecto_integrador.mainActivities.profile.ProfileActivity;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, CollectUserData.Comunicacion {
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
    private CollectUserData.Comunicacion a = this;

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
        CollectUserData.takeData(a);
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
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), NotificationsFragment.class));
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
            CollectUserData.takeData(a);
        }
    }

    private void drawLocation(double latitud, double longitud, String nombre,String userID) {
        //Aqui se pasan las coordenadas
        LatLng ubi = new LatLng(latitud, longitud);
        String currentUID = u.getUid();
        if(userID.equals(currentUID)) {
            //Aqui dirigimos la camara a la ubicacion
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubi, 13));
            //Esto es el marcador con el titulo de ubicacion
            mMap.addMarker(new MarkerOptions().position(ubi).title(nombre));
        }else{
            //Aqui dirigimos la camara a la ubicacion
            mMap.animateCamera(CameraUpdateFactory.newLatLng(ubi));
            //Esto es el marcador con el titulo de ubicacion
            mMap.addMarker(new MarkerOptions().position(ubi).title(nombre));
        }
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
        User uBueno = new User();
        String currentUID = u.getUid();
        mMap.clear();
        for (User user : users) {
            if (user.getUserId().equals(currentUID)){
                uBueno = user;
            }else{
                if ((user.getLatitud() == 0)){
                }else {
                    drawLocation(user.getLatitud(), user.getLongitud(), user.getUsername(), user.getUserId());
                }
            }
        }
        drawLocation(uBueno.getLatitud(),uBueno.getLongitud(),uBueno.getUsername(),uBueno.getUserId());
    }
}
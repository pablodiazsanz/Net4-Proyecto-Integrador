package com.pass.net4_proyecto_integrador;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //Barra de abajo
    private BottomNavigationView bnv;
    //Para el Mapa
    private GoogleMap mMap;
    private OnMapReadyCallback omrc = this;
    private Location currentlocation;
    private LocationManager lm;
    private static final int REQUEST_CODE = 101;
    private double longitud;
    private double latitud;
    //context
    private Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        bnv = findViewById(R.id.bottomNavigationBar);
        bnv.setBackground(null);
        bnv.getMenu().getItem(2).setEnabled(false);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Aqui prguntaremos si quiere dar permiso de ubicacion a la aplicacion en caso de que no tenga ira al metodo onRequestPermissionsResult(),
        //si ya tiene los permiso ira directamente a obtenerLocalizacion()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        } else {
            obtenerLocalizacion();
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
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Aqui revisamos si estan los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Aqui recojemos las ultimas ubicaciones
        currentlocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Este toast es para que muestre el mensaje de que busca una ubicacion
        Toast toast = Toast.makeText(contexto, "Buscando Ubicacion.....", Toast.LENGTH_LONG);
        toast.show();
        //Aqui ponemos un oyesnte a la ubicacion y en caso de que sea nulo le decimos que ponga la ultima encontrada
        //y si no que compruebe si la latitud y longitud no son nulas
        //en caso de que sean busca otra vez y si las encuentra que lo marque en el mapa
        LocationListener oyente_localizaciones = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (location != null) {
                    location = currentlocation;
                    if (currentlocation == null){
                        obtenerLocalizacion();
                    }else{
                        latitud = currentlocation.getLatitude();
                        longitud = currentlocation.getLongitude();
                        // Para llamar al metodo onMapReady
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(omrc);
                        lm.removeUpdates(this);
                    }
                }else{
                    latitud = currentlocation.getLatitude();
                    longitud = currentlocation.getLongitude();
                    // Para llamar al metodo onMapReady
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(omrc);
                }
            }
        };
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, oyente_localizaciones);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Aqui se pasan las coordenadas
        LatLng ubi = new LatLng(latitud, longitud);
        //Aqui dirigimos la camara a la ubicacion
        mMap.animateCamera(CameraUpdateFactory.newLatLng(ubi));
        //El zoom que va de 0 a 21
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubi, 10));
        //Esto es el marcador con el titulo de ubicacion
        mMap.addMarker(new MarkerOptions().position(ubi).title("Mi Ubicacion"));
    }
}
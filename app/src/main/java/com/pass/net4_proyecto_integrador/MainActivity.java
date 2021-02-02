package com.pass.net4_proyecto_integrador;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private BottomNavigationView bnv;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private OnMapReadyCallback omrc = this;
    private Location currentlocation;
    private static final int REQUEST_CODE = 101;
    private double longitud;
    private double latitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnv = findViewById(R.id.bottomNavigationBar);
        bnv.setBackground(null);
        bnv.getMenu().getItem(2).setEnabled(false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    void fetchLastLocation() {
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
        //Aqui revisamos si estan los permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Aqui recojemos las ultimas ubicaciones
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentlocation = location;
                    latitud = currentlocation.getLatitude();
                    longitud = currentlocation.getLongitude();
                    // Para llamar al metodo onMapReady
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(omrc);
                }
            }
        });
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
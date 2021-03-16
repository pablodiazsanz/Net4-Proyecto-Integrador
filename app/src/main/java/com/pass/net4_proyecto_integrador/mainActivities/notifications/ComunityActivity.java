package com.pass.net4_proyecto_integrador.mainActivities.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.User;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.profile.ProfileActivity;

import java.util.ArrayList;

public class ComunityActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    BottomNavigationView bnv;

    Iterable<DataSnapshot> datos;
    ArrayList<User> listaUsuarios;
    RecyclerView rv;
    RecyclerView.LayoutManager gestor;
    Community_activity_adapter miAdaptador;
    private SearchView svSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        svSearch = findViewById(R.id.svSearch);
        initListener();

        bnv = findViewById(R.id.nav_community);
        bnv.setSelectedItemId(R.id.navigation_community);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_maps:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_activity:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_help_alert:
                        startActivity(new Intent(getApplicationContext(), HelpAlertActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_community:
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        rv = findViewById(R.id.rvUusuarios);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        gestor = new LinearLayoutManager(this);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datos = snapshot.getChildren();
                listaUsuarios = new ArrayList<>();
                for (DataSnapshot d : datos){
                    Log.d("ERRORACO", snapshot.toString());
                    listaUsuarios.add(d.getValue(User.class));
                    insertarDatos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertarDatos() {
        miAdaptador = new Community_activity_adapter(listaUsuarios);
        rv.setLayoutManager(gestor);
        rv.setAdapter(miAdaptador);
    }

    private void initListener(){
        svSearch.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        miAdaptador.filter(newText);
        return false;
    }
}


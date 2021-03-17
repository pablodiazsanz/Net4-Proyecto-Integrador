package com.pass.net4_proyecto_integrador.extraActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.Evento;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.User;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UsersProfileActivity extends AppCompatActivity {

    Iterable<DataSnapshot> datos;
    ArrayList<Evento> listaEventos;
    RecyclerView rv;
    RecyclerView.LayoutManager gestor;
    UsersProfileAdapter miAdaptador;

    TextView tituloPerfil, descripcionPerfil, txtLocation;
    ImageView imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);

        rv = findViewById(R.id.rvEventosPerfil);
        tituloPerfil = findViewById(R.id.tituloPerfil);
        descripcionPerfil = findViewById(R.id.descripcionPerfil);
        imgPerfil = findViewById(R.id.imgPerfilPerfil);
        txtLocation = findViewById(R.id.txt_location_users);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userId");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference eventRef = database.getReference("Events");
        DatabaseReference userRef = database.getReference("Users").child(userID);

        Glide.with(getApplicationContext())
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/profilepics%2F" + userID + ".jpg?alt=media&token=dcb65d07-cace-45b4-8fb7-e38880be36ce"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .circleCrop()
                .into(imgPerfil);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                tituloPerfil.setText(u.getUsername());
                descripcionPerfil.setText(u.getDescription());
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(u.getLatitud(), u.getLongitud(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                txtLocation.setText(cityName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gestor = new LinearLayoutManager(this);

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datos = snapshot.getChildren();
                listaEventos = new ArrayList<>();
                for (DataSnapshot d : datos) {
                    Log.d("ERRORACO", snapshot.toString());
                    if (d.getKey().contains(userID)) {
                        listaEventos.add(d.getValue(Evento.class));
                        insertarDatos();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void insertarDatos() {
        miAdaptador = new UsersProfileAdapter(listaEventos);
        rv.setLayoutManager(gestor);
        rv.setAdapter(miAdaptador);
    }
}
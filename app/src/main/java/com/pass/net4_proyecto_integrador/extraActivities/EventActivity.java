package com.pass.net4_proyecto_integrador.extraActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class EventActivity extends AppCompatActivity {

    ImageView imgEvento, imgPerfilEvento;
    TextView tituloEvento, descripcionEvento, fechaEvento, usernameEvento;
    String eventUserId, eventTitulo;
    Button btnPerfil, btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        imgEvento = findViewById(R.id.imgEvento);
        imgPerfilEvento = findViewById(R.id.imgPerfilEvento);
        tituloEvento = findViewById(R.id.tituloEvento);
        descripcionEvento = findViewById(R.id.descripcionEvento);
        fechaEvento = findViewById(R.id.fechaEvento);
        usernameEvento = findViewById(R.id.usernamePerfilEvento);
        btnPerfil = findViewById(R.id.btn_mosperf);
        btnChat = findViewById(R.id.btn_chat);

        Intent intent = getIntent();
        eventUserId = intent.getStringExtra("eventUserId");
        eventTitulo = intent.getStringExtra("eventTitulo");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference eventRef = rootRef.child("Events").child(eventUserId+"-"+eventTitulo);
        DatabaseReference userRef = rootRef.child("Users");

        Glide.with(getApplicationContext())
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/profilepics%2F" + eventUserId + ".jpg?alt=media&token=dcb65d07-cace-45b4-8fb7-e38880be36ce"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .circleCrop()
                .into(imgPerfilEvento);

        Glide.with(getApplicationContext())
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/eventspics%2F" + eventUserId+"-"+eventTitulo + ".jpg?alt=media&token=26419bcf-488c-4c50-802a-8088e2c092b1"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                .into(imgEvento);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId : snapshot.getChildren()) {
                    if (keyId.child("userId").getValue().equals(eventUserId)) {
                        usernameEvento.setText(keyId.child("username").getValue(String.class));
                        Log.v("username", keyId.child("username").getValue(String.class));
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Evento e = snapshot.getValue(Evento.class);
                tituloEvento.setText(e.getTitulo());
                descripcionEvento.setText(e.getDescripcion());
                fechaEvento.setText(e.getFecha());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UsersProfileActivity.class);
                i.putExtra("userId", eventUserId);
                startActivity(i);
            }
        });
    }
}
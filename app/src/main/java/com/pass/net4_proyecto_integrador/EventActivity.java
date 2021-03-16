package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity {

    ImageView imgEvento, imgPerfilEvento;
    TextView tituloEvento, descripcionEvento, fechaEvento, usernameEvento;

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
    }
}
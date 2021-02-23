package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class ChangeEmailActivity extends AppCompatActivity {

    TextView txtCancel;
    Button btnSavePersonalInf;
    ImageView backgroundChangePersonalInf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        txtCancel = findViewById(R.id.txt_cancelChangeEmail);
        btnSavePersonalInf = findViewById(R.id.btn_acceptChangePersonalInf);
        backgroundChangePersonalInf = findViewById(R.id.backgroundChangePersonalInfo);
        Glide.with(this)
                .load("android.resource://" + getPackageName() + "/"+ R.drawable.change_pwd_email_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(backgroundChangePersonalInf);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeEmailActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        });


        btnSavePersonalInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeEmailActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        });
    }
}
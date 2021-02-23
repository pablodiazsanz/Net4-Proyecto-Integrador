package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class ChangePasswordActivity extends AppCompatActivity {

    TextView txt_cancelChange;
    Button btn_saveChange;
    ImageView backgroundChangePasswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        txt_cancelChange = findViewById(R.id.txt_cancel);
        btn_saveChange = findViewById(R.id.btn_acceptChangePassword);

        backgroundChangePasswd = findViewById(R.id.backgroundChangePswd);

        Glide.with(this)
                .load("android.resource://" + getPackageName() + "/"+ R.drawable.change_pwd_email_background)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(backgroundChangePasswd);

        txt_cancelChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        });

        btn_saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        });
    }
}
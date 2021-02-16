package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChangeEmailActivity extends AppCompatActivity {

    TextView txtCancel;
    Button btnSavePersonalInf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        txtCancel = findViewById(R.id.txt_cancelChangeEmail);
        btnSavePersonalInf = findViewById(R.id.btn_acceptChangePersonalInf);

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
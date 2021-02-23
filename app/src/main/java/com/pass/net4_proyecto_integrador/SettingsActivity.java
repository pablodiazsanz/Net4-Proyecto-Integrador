package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.pass.net4_proyecto_integrador.settingsActivities.ChangeEmailActivity;
import com.pass.net4_proyecto_integrador.settingsActivities.ChangePasswordActivity;
import com.pass.net4_proyecto_integrador.settingsActivities.PDFActivity;

public class SettingsActivity extends AppCompatActivity {
    private LinearLayout cerrarSesion;
    private LinearLayout politicaDePrivacidad;
    private LinearLayout contacto;

    private LinearLayout changePssword;
    private LinearLayout changePersonalInf;

    //Google
    private GoogleSignInClient mGoogleSignInClient;
    //Facebook
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cerrarSesion = findViewById(R.id.cerrar_sesion);
        politicaDePrivacidad = findViewById(R.id.politica_privacidad);
        contacto = findViewById(R.id.contacto);
        changePssword = findViewById(R.id.cambiar_contraseña);
        changePersonalInf = findViewById(R.id.changePersonalInfo);

        changePssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }

        });

        changePersonalInf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }

        });

        mAuth = LoginActivity.recogerInstancia();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //****************** BUTTONS ACTIONS ****************

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        politicaDePrivacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, PDFActivity.class);
                startActivity(intent);
            }
        });

        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogContact();
            }
        });
    }

    /**
     * In this method, the password recovery screen has been implemented
     * as an alert dialog.
     */
    private void showAlertDialogContact() {
        // Here we setup the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.activity_contact,
                null));

        // Here we create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
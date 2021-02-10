package com.pass.net4_proyecto_integrador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class provides functionality to the activity_login layout.
 * @version 1.0
 * @author Sebastian Huete, Sergio Turdasan, Alvaro Tunon y Pablo Diaz.
 */
public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView txt_forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button boton = findViewById(R.id.mi_button);

        // Here we load the items of the layout
        btn_login = findViewById(R.id.btn_login);
        txt_forgot_password = findViewById(R.id.txt_forget);

        loginButtonClick();

        forgotPasswordClick();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * In this method we have put the setOnClickListener of the login button.
     */
    private void loginButtonClick(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /**
     * In this method we have implemented the setOnClickListener of the
     * forgot password text field.
     */
    private void forgotPasswordClick(){
        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogForgotPassword();
            }
        });
    }

    /**
     * In this method we have implemented the animation to the sidebar that moves
     * from login to register.
     * @param view
     */
    public void loginToRegisterSidebarClick(View view){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
    }

    /**
     * In this method, the password recovery screen has been implemented
     * as an alert dialog.
     */
    private void showAlertDialogForgotPassword() {
        // Here we setup the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.activity_email_recovery,
                null));

        // Here we create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

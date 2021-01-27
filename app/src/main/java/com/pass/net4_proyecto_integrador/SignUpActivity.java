package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class SignUpActivity extends AppCompatActivity {

    private ImageView signUpImg;
    private TextView txtLogIn;
    protected  SignUpActivity signUpAct = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpImg = findViewById(R.id.img_start);
        txtLogIn = findViewById(R.id.txt_SignUpToLogIn);
        String pathIconSignUp = "https://images.unsplash.com/photo-1528763380143-65b3ac89a3ff?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=635&q=80";


        signUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpImg = findViewById(R.id.img_start);
                signUpImg.clearFocus();

                Glide.with(signUpAct)
                        .load(pathIconSignUp)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .circleCrop()
                        .into(signUpImg);
            }
        });

        txtLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });
    }
    }

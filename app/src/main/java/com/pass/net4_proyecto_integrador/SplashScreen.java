package com.pass.net4_proyecto_integrador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    private Animation topAnimation, bottomAnimation;
    private ImageView icon, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation_splash_screen);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_splash_screen);

        icon = findViewById(R.id.iv_icon);
        name = findViewById(R.id.iv_name);

        icon.setAnimation(topAnimation);
        name.setAnimation(bottomAnimation);

        openApp(true);
    }

    private void openApp(boolean locationPermission) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen
                        .this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }
}

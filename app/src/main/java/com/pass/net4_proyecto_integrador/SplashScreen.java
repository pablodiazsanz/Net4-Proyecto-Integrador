package com.pass.net4_proyecto_integrador;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    private Animation topAnimation, bottomAnimation;
    private ImageView icon, name;
    private View screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quitarBarraNavegacionEstado();

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

    private void quitarBarraNavegacionEstado() {
        screen = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        screen.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_splash_screen);
    }
}

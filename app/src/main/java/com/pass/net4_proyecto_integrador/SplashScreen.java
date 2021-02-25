package com.pass.net4_proyecto_integrador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * This class provides functionality to the activity_splash_screen layout.
 * @version 1.0
 * @author Sebastian Huete, Sergio Turdasan, Alvaro Tunon y Pablo Diaz.
 */
public class SplashScreen extends Activity {

    private Animation topAnimation, bottomAnimation, alphaAnimation;
    private ImageView icon, name, logoSplash;
    private View screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideNavigationBarStatusBar();

        // Here we load the top and bottom animations
        //topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation_splash_screen);
        //bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation_splash_screen);
        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_splash_screen);

        // Here we look for the views in the layout
        //icon = findViewById(R.id.iv_icon);
        //name = findViewById(R.id.iv_name);
        logoSplash = findViewById(R.id.logo_splash);

        // Here we set the animations on the views
        //icon.setAnimation(topAnimation);
        //name.setAnimation(bottomAnimation);
        logoSplash.setAnimation(alphaAnimation);

        openApp(true);
    }

    /**
     * This method is used to change the Splash Screen to the Login Screen.
     * @param locationPermission
     */
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

    /**
     * This method is used to hide the navigation bar and the status bar. Method found
     * in the Android Developers Documentation:
     * https://developer.android.com/training/system-ui/navigation?hl=es
     */
    private void hideNavigationBarStatusBar () {
        screen = getWindow().getDecorView();
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |View.SYSTEM_UI_FLAG_FULLSCREEN;
        screen.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_splash_screen);
    }
}

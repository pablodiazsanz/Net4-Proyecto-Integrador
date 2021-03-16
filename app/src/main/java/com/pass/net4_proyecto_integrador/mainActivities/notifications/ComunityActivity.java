package com.pass.net4_proyecto_integrador.mainActivities.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.profile.ProfileActivity;

public class ComunityActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunity);

        bnv = findViewById(R.id.nav_view_chat);

        bnv.setSelectedItemId(R.id.navigation_chat);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_maps:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_activity:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_help_alert:
                        startActivity(new Intent(getApplicationContext(), HelpAlertActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_chat:
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}
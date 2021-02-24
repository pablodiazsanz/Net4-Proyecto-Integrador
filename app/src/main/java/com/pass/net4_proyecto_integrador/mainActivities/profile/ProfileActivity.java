package com.pass.net4_proyecto_integrador.mainActivities.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.pass.net4_proyecto_integrador.SettingsActivity;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.notifications.NotificationsFragment;

public class ProfileActivity  extends AppCompatActivity {

    BottomNavigationView bnv;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    TextView name;
    String nom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bnv = findViewById(R.id.nav_view_profile);
        bnv.setSelectedItemId(R.id.navigation_profile);
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
                        startActivity(new Intent(getApplicationContext(), NotificationsFragment.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_profile:
                        return true;
                }
                return false;
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_id);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(myToolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ProfileAdapterActivity adapter = new ProfileAdapterActivity(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new ProfileAboutFragmentActivity(),"About");
        adapter.AddFragment(new ProfileValorationsFragmentActivity(),"Valorations");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        name = findViewById(R.id.textViewNameProfile);

        LocalBroadcastManager.getInstance(this).registerReceiver(
                messageReceiver, new IntentFilter("SEND_PERSONAL_DATA"));

    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            nom = intent.getStringExtra("name");
            putName(nom);
        }
    };

    private void putName(String nombre) {
        name.setText(nombre);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_button){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile_fragment, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

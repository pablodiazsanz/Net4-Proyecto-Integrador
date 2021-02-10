package com.pass.net4_proyecto_integrador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ProfileAdapterActivity adapter = new ProfileAdapterActivity(getSupportFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new PorfileAboutFragmentActivity(),"About");
        adapter.AddFragment(new ProfileMultimediaFragmenttActivity(),"Multimedia");
        adapter.AddFragment(new ProfileValorationsFragmentActivity(),"Valorations");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
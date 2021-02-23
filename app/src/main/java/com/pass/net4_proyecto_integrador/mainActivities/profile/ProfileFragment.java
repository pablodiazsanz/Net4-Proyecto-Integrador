package com.pass.net4_proyecto_integrador.mainActivities.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.SettingsActivity;

public class ProfileFragment extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar myToolbar;

    public ProfileFragment() {
    }
    public static ProfileFragment getInstance(){
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.activity_profile,container,false);
        viewPager = myFragment.findViewById(R.id.viewpager_id);
        tabLayout = myFragment.findViewById(R.id.tabLayout_id);
        myToolbar = (Toolbar) myFragment.findViewById(R.id.toolbar_profile);
        setHasOptionsMenu(true);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager){
        ProfileAdapterActivity adapter = new ProfileAdapterActivity(getChildFragmentManager());
        //Adding Fragments
        adapter.AddFragment(new ProfileAboutFragmentActivity(),"About");
        adapter.AddFragment(new ProfileValorationsFragmentActivity(),"Valorations");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings_button){
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_profile_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

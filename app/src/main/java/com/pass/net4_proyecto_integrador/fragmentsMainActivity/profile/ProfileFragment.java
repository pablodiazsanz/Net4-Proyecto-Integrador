package com.pass.net4_proyecto_integrador.fragmentsMainActivity.profile;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.fragmentsMainActivity.notifications.NotificationsViewModel;

public class ProfileFragment extends Fragment {

    View myFragment;
    ViewPager viewPager;
    TabLayout tabLayout;

    public ProfileFragment() {
    }
    public static ProfileFragment getInstance(){
        return new ProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_profile,container,false);
        viewPager = myFragment.findViewById(R.id.viewpager_id);
        tabLayout = myFragment.findViewById(R.id.tabLayout_id);
        return myFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
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
        adapter.AddFragment(new PorfileAboutFragmentActivity(),"About");
        adapter.AddFragment(new ProfileMultimediaFragmenttActivity(),"Multimedia");
        adapter.AddFragment(new ProfileValorationsFragmentActivity(),"Valorations");
        //Adapter Setup
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}

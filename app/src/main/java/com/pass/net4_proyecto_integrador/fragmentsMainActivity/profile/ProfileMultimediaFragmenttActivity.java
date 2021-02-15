package com.pass.net4_proyecto_integrador.fragmentsMainActivity.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pass.net4_proyecto_integrador.R;

public class ProfileMultimediaFragmenttActivity extends Fragment {

    View view;
    public ProfileMultimediaFragmenttActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_multimedia_fragmentt,container,false);
        return view;
    }
}
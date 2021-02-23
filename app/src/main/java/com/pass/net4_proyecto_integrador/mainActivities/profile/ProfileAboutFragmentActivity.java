package com.pass.net4_proyecto_integrador.mainActivities.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pass.net4_proyecto_integrador.R;

public class ProfileAboutFragmentActivity extends Fragment {
    View view;
    public ProfileAboutFragmentActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_porfile_about_fragment,container,false);
        return view;
    }
}
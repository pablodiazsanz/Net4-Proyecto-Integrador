package com.pass.net4_proyecto_integrador.mainActivities.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.pass.net4_proyecto_integrador.CollectUserData;
import com.pass.net4_proyecto_integrador.R;

public class ProfileAboutFragmentActivity extends Fragment {
    private View view;
    private TextView nombre;
    private TextView apellido;
    private TextView tlf;
    private TextView email;
    private TextView contrasenia;

    public ProfileAboutFragmentActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_porfile_about_fragment,container,false);
        if(view != null){
            nombre = view.findViewById(R.id.txt_name);
            apellido = view.findViewById(R.id.txt_surname);
            tlf = view.findViewById(R.id.txt_phone);
            email = view.findViewById(R.id.txt_email);
            contrasenia = view.findViewById(R.id.txt_pasword);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String datos = CollectUserData.getDatos();
        String[] data = datos.split("-");
        if (data[0].equals("G")) {
            nombre.setText(data[1]);
            email.setText(data[2]);
            apellido.setText("");
            tlf.setText("Inserte un telefono");
            contrasenia.setText("");
        }
    }
}
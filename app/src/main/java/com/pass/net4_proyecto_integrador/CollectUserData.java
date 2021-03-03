package com.pass.net4_proyecto_integrador;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public class CollectUserData {
    private static String datos;

    //Recoger datos
    public static void updateUI(FirebaseUser user, String letra) {
        if(user != null){
            String nombre = user.getDisplayName();
            String email = user.getEmail();
            String photo = user.getPhotoUrl().toString();
            setDatos(letra + "-" + nombre + "-" + email + "-" + photo);
        }
    }

    public static String getDatos() {
        return datos;
    }

    public static void setDatos(String datos) {
        CollectUserData.datos = datos;
    }
}

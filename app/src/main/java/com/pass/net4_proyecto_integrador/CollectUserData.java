package com.pass.net4_proyecto_integrador;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public class CollectUserData {
    private static String datos;

    //Recoger datos
    public static void updateUIGoogle(Context context ,FirebaseUser user, String letra) {
        GoogleSignInAccount sign = GoogleSignIn.getLastSignedInAccount(context);
        if(sign != null){
            String nombre = sign.getDisplayName();
            String email = sign.getEmail();
            String photo = sign.getPhotoUrl().toString();
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

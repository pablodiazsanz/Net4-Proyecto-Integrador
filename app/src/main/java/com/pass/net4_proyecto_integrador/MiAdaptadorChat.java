package com.pass.net4_proyecto_integrador;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;

public class MiAdaptadorChat extends RecyclerView.Adapter<com.pass.net4_proyecto_integrador.MiAdaptadorChat.MiContenedorDeVistas> {
    private ArrayList<Mensajes> lista;
    private View vista;

    public MiAdaptadorChat(ArrayList<Mensajes> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MiContenedorDeVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view,parent,false);
        MiContenedorDeVistas contenedor = new MiContenedorDeVistas(vista);
        return contenedor;
    }

    @Override
    public void onBindViewHolder(@NonNull MiContenedorDeVistas holder, int position) {
        Mensajes m = lista.get(position);
        holder.txt_mensaje.setText(m.getMensaje());
        holder.txt_username.setText(m.getUsername());
        holder.txt_hora.setText(m.getHora());
        Glide.with(vista)
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/profilepics%2F" + m.getUid_user() + ".jpg?alt=media&token=dcb65d07-cace-45b4-8fb7-e38880be36ce"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .circleCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MiContenedorDeVistas extends RecyclerView.ViewHolder{
        public TextView txt_mensaje,txt_username,txt_hora;
        public ImageView img;

        public MiContenedorDeVistas(View vista) {
            super(vista);
            this.txt_mensaje = vista.findViewById(R.id.text_mensaje_caht_View);
            this.txt_username = vista.findViewById(R.id.text_username_chat_View);
            this.txt_hora = vista.findViewById(R.id.text_hora_chat_View);
            this.img = vista.findViewById(R.id.img_chat_view);
        }
    }
}

package com.pass.net4_proyecto_integrador.extraActivities;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pass.net4_proyecto_integrador.Evento;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardAdapter;

import java.util.ArrayList;

public class UsersProfileAdapter extends RecyclerView.Adapter<DashboardAdapter.MiContenedorDeVistas>{

    private ArrayList<Evento> listaEventos;
    View vista;

    public UsersProfileAdapter(ArrayList<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public DashboardAdapter.MiContenedorDeVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_dashboard, parent, false);
        DashboardAdapter.MiContenedorDeVistas contenedor = new DashboardAdapter.MiContenedorDeVistas(vista);
        return contenedor;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.MiContenedorDeVistas holder, int position) {
        Evento e = listaEventos.get(position);
        holder.tvTitulo.setText(e.getTitulo());
        holder.tvDescripcion.setText(e.getDescripcion());
        holder.tvFecha.setText(e.getFecha());
        holder.tvUrgencia.setText(e.getGradoUrgencia());

        Glide.with(vista)
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/eventspics%2F" + e.getUserId()+"-" + e.getTitulo() + ".jpg?alt=media&token=26419bcf-488c-4c50-802a-8088e2c092b1"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                //.transition(DrawableTransitionOptions.withCrossFade(300))
                //.circleCrop()
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EventActivity.class);
                intent.putExtra("eventUserId", e.getUserId());
                intent.putExtra("eventTitulo", e.getTitulo());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public static class MiContenedorDeVistas extends RecyclerView.ViewHolder{
        public TextView tvTitulo, tvDescripcion, tvFecha, tvUrgencia;
        public ImageView imageView;
        public CardView cardView;
        public MiContenedorDeVistas(View vista) {
            super(vista);
            this.tvTitulo = vista.findViewById(R.id.tituloCV);
            this.tvDescripcion = vista.findViewById(R.id.descripcionCV);
            this.tvFecha = vista.findViewById(R.id.fechaCV);
            this.tvUrgencia = vista.findViewById(R.id.urgenciaCV);
            this.imageView = vista.findViewById(R.id.imageviewCV);
            this.cardView = vista.findViewById(R.id.cardView);
        }
    }
}

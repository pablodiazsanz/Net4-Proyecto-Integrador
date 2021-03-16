package com.pass.net4_proyecto_integrador.mainActivities.notifications;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.User;
import com.pass.net4_proyecto_integrador.extraActivities.EventActivity;
import com.pass.net4_proyecto_integrador.extraActivities.UsersProfileActivity;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MiContenedorDeVistas>{

    private ArrayList<User> listaUsuarios;
    private List<User> originalItems;
    View vista;

    public CommunityAdapter(ArrayList<User> listaEventos) {
        this.listaUsuarios = listaEventos;
        this.originalItems = new ArrayList<>();
        originalItems.addAll(listaEventos);
    }

    @NonNull
    @Override
    public CommunityAdapter.MiContenedorDeVistas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_comunity_items, parent, false);
        CommunityAdapter.MiContenedorDeVistas contenedor = new CommunityAdapter.MiContenedorDeVistas(vista);
        return contenedor;
    }


    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.MiContenedorDeVistas holder, int position) {
        User u = listaUsuarios.get(position);
        holder.txt_username.setText(u.getUsername());
        holder.txt_name.setText(u.getName());

        Glide.with(vista)
                .load(Uri.parse("https://firebasestorage.googleapis.com/v0/b/net4-515ff.appspot.com/o/profilepics%2F" + u.getUserId()+ ".jpg?alt=media&token=dcb65d07-cace-45b4-8fb7-e38880be36ce"))
                .placeholder(R.drawable.monster_interrogation_add_icon)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .circleCrop()
                .into(holder.img_profile);

        holder.rlCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UsersProfileActivity.class);
                intent.putExtra("userId", u.getUserId());
                v.getContext().startActivity(intent);
            }
        });
    }
   // gs://net4-515ff.appspot.com/profilepics/X5R5ti29unZaXOyJt53OYJslu843.jpg
    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
    public void filter(final String strSearch) {
        if (strSearch.length() == 0) {
            listaUsuarios.clear();
            listaUsuarios.addAll(originalItems);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                listaUsuarios.clear();
                List<User> collect = originalItems.stream()
                        .filter(i -> i.getUsername().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());

                listaUsuarios.addAll(collect);
            }
            else {
                listaUsuarios.clear();
                for (User i : originalItems) {
                    if (i.getUsername().toLowerCase().contains(strSearch)) {
                        listaUsuarios.add(i);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class MiContenedorDeVistas extends RecyclerView.ViewHolder{
        public TextView txt_username, txt_name;
        public ImageView img_profile;
        public RelativeLayout rlCommunity;
        public MiContenedorDeVistas(View vista) {
            super(vista);
            this.txt_username = vista.findViewById(R.id.username);
            this.txt_name = vista.findViewById(R.id.fullname);
            this.img_profile = vista.findViewById(R.id.img_profile);
            this.rlCommunity = vista.findViewById(R.id.rl_community);
        }
    }
}

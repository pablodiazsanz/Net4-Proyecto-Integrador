package com.pass.net4_proyecto_integrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardAdapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ChatActivity extends AppCompatActivity {
    private EditText txtMensaje;
    private Toolbar toolbar;
    private RecyclerView recyclerViewChat;
    private Button btnEnviar;
    private MiAdaptadorChat adaptador;
    private String username;
    private RecyclerView.LayoutManager gestor;
    private ArrayList<Mensajes> listaMensajes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        txtMensaje = findViewById(R.id.edit_txt_chat);
        toolbar = findViewById(R.id.toolbar_chat);
        recyclerViewChat = findViewById(R.id.recycler_chat);
        btnEnviar = findViewById(R.id.btn_chat);

        gestor = new LinearLayoutManager(this);

        Intent intent = getIntent();
        String userUId = intent.getStringExtra("userId");
        String tituloEvent = intent.getStringExtra("tituloEvent");

        toolbar.setTitle("Chat " + tituloEvent);

        DatabaseReference referenceChat = FirebaseDatabase.getInstance().getReference();
        DatabaseReference referenceMessage = referenceChat.child("Chats").child(userUId + "-" + tituloEvent);
        DatabaseReference referenceUser = referenceChat.child("Users").child(LoginActivity.USERUID);

        referenceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                username = u.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot>datos = snapshot.getChildren();
                listaMensajes = new ArrayList<>();
                for (DataSnapshot d : datos){
                    Log.d("ERRORACO", snapshot.toString());
                    listaMensajes.add(d.getValue(Mensajes.class));
                    insertarDatos();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] hora = cogerHora();
                Mensajes m = new Mensajes(txtMensaje.getText().toString().trim(),username,LoginActivity.USERUID,hora[0]);
                referenceMessage.child(hora[1]).setValue(m);
                txtMensaje.setText("");
            }
        });

        recyclerViewChat.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if ( bottom < oldBottom) {
                    recyclerViewChat.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerViewChat.smoothScrollToPosition(adaptador.getItemCount()-1);
                        }
                    }, 100);
                }
            }
        });
    }

    private void insertarDatos() {
        adaptador = new MiAdaptadorChat(listaMensajes);
        recyclerViewChat.setLayoutManager(gestor);
        recyclerViewChat.setAdapter(adaptador);
        setScrollBar();
    }

    private String[] cogerHora() {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        Date date = new Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = sdf.format(date);
        String timeInMiliseconds = String.valueOf(stamp.getTime());
        String[] array = {formattedDate, timeInMiliseconds};
        return array;
    }

    private void setScrollBar() {
        recyclerViewChat.scrollToPosition(adaptador.getItemCount()-1);
    }
}
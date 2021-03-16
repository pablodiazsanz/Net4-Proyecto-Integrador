package com.pass.net4_proyecto_integrador;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CollectEventData {
    private static FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();
    private static DatabaseReference myDatabaseReference = myDatabase.getReference("Events");

    public static void takeData(CollectEventData.Comunicacion comunicacion){
        List<Evento> listaEventos = new ArrayList<>();
        myDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> datos = snapshot.getChildren();
                for (DataSnapshot d: datos) {
                    Evento e = d.getValue(Evento.class);
                    listaEventos.add(e);
                }
                comunicacion.sendDataEvento(listaEventos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public interface Comunicacion{
        void sendDataEvento(List<Evento> listaEvento);
    }
}


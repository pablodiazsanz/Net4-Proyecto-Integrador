package com.pass.net4_proyecto_integrador.mainActivities.helpAlert;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pass.net4_proyecto_integrador.extraActivities.EventActivity;
import com.pass.net4_proyecto_integrador.Evento;
import com.pass.net4_proyecto_integrador.LoginActivity;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.notifications.ComunityActivity;
import com.pass.net4_proyecto_integrador.mainActivities.profile.ProfileActivity;

import java.util.Date;

public class HelpAlertActivity extends AppCompatActivity {

    BottomNavigationView bnv;
    EditText etTitulo, etDescripcion;
    Spinner spUrgencia;
    Button btnSubirFoto, btnSolicitarAyuda;
    int posicionUrgencia;
    ImageView ivPedirAyuda;
    Uri imageUri;
    String profileImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_alert);

        bnv = findViewById(R.id.nav_view_help_alert);
        bnv.setSelectedItemId(R.id.navigation_help_alert);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_maps:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_activity:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_help_alert:
                        return true;
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), ComunityActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        etTitulo = findViewById(R.id.editTextTitulo);
        etDescripcion = findViewById(R.id.editTextDescripcionn);
        spUrgencia = findViewById(R.id.spinner_urgencia);
        btnSubirFoto = findViewById(R.id.btn_subir_foto);
        btnSolicitarAyuda = findViewById(R.id.btn_solicitar);
        ivPedirAyuda = findViewById(R.id.imageview_pedirayuda);

        String[] tipoUrgencia = {"No es urgente","Urgente","Muy urgente"};
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,tipoUrgencia);
        spUrgencia.setAdapter(adaptador);

        spUrgencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posicionUrgencia = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                    } else {
                        ActivityCompat.requestPermissions(HelpAlertActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                    }
                } else {

                }
            }
        });

        btnSolicitarAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarDatosFirebase();
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("eventUserId", LoginActivity.USERUID);
                intent.putExtra("eventTitulo", etTitulo.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void insertarDatosFirebase() {
        String titulo = etTitulo.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String[] tipoUrgencia = {"No es urgente","Urgente","Muy urgente"};
        Date objDate = new Date();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Events");
        Evento e = new Evento(LoginActivity.USERUID, titulo, descripcion, tipoUrgencia[posicionUrgencia], objDate.toString());
        myRef.child(LoginActivity.USERUID+"-"+titulo).setValue(e);
        uploadImageToFirebaseStorage();
        Toast.makeText(getApplicationContext(), "Ayuda Pedida correctamente", Toast.LENGTH_LONG).show();
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("eventspics/" + LoginActivity.USERUID + "-" + etTitulo.getText().toString().trim()+ ".jpg");

        if (imageUri != null) {
            profileImageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageUrl = taskSnapshot.getUploadSessionUri().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("PUTAIMG", e.getLocalizedMessage());
                }
            });
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == 101) {
            imageUri = data.getData();



            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .circleCrop()
                    .into(ivPedirAyuda);
        } else {
            Log.i("RAG", "Result: " + resultCode);
            Toast.makeText(this, "You did not choose any photo", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
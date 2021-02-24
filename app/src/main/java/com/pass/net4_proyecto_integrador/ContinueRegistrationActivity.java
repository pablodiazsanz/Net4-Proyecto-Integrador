package com.pass.net4_proyecto_integrador;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;

import java.io.File;
import java.io.IOException;

/**
 * This class provides functionality to the activity_register layout.
 *
 * @author Sebastian Huete, Sergio Turdasan, Alvaro Tunon y Pablo Diaz.
 * @version 1.0
 */
public class ContinueRegistrationActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSON_CODE = 100;
    private static final int REQUEST_IMAGE_GALLERY = 101;
    private Uri imageUri;
    private ImageView galleryImage;
    File fileImage;
    Bitmap bitmap;
    Context context = this;

    TextInputLayout name, phoneNumber, description;
    Button btnRegister;

    FirebaseDatabase myDatabase;
    DatabaseReference myDatabaseReference;
    FirebaseAuth firebaseAuth;

    String username, email, pwd, profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_register);

        galleryImage = (ImageView) findViewById(R.id.image_add);
        name = findViewById(R.id.textInputName);
        phoneNumber = findViewById(R.id.textInputPhoneNumber);
        description = findViewById(R.id.textInputDescriptionUser);
        btnRegister = findViewById(R.id.continueRegistrationButton);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        email = intent.getStringExtra("Email");
        pwd = intent.getStringExtra("Password");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeRegister();
            }
        });


        Glide.with(this)
                .load("android.resource://" + getPackageName() + "/" + R.drawable.monster_interrogation_add_icon)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(galleryImage);

        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(ContinueRegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        openGallery();
                    } else {
                        ActivityCompat.requestPermissions(ContinueRegistrationActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSON_CODE);
                    }
                } else {

                }

            }
        });
    }

    private void completeRegister() {
        String nombre = name.getEditText().getText().toString();
        String numero = phoneNumber.getEditText().getText().toString();
        String descripcion = description.getEditText().getText().toString();

        if (TextUtils.isEmpty(nombre)) {
            name.setError("Enter your name");
            return;
        } else {
            myDatabase = FirebaseDatabase.getInstance();
            myDatabaseReference = myDatabase.getReference("Users");

            User nuevoUsuario = new User(username, nombre, email, numero, descripcion);

            myDatabaseReference.child(username).setValue(nuevoUsuario);

            firebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        saveUserInformation();

                        Toast.makeText(getApplicationContext(), "Signed up succesfully", Toast.LENGTH_LONG).show();
                        Intent accessIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        accessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        accessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(accessIntent);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(), "This user is already registered", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Signed up failed", Toast.LENGTH_LONG).show();
                            Log.d("ERROREG", task.getException().toString());
                        }
                    }
                }
            });
        }
    }

    private void saveUserInformation() {
        String displayName = name.getEditText().getText().toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(imageUri)
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    /**
     * this method is useful for get the Gallery Image
     * It have permissions
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY) {
            imageUri = data.getData();

            uploadImageToFirebaseStorage();

            Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .circleCrop()
                    .into(galleryImage);
        } else {
            Log.i("RAG", "Result: " + resultCode);
            Toast.makeText(this, "You did not choose any photo", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageReference = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSON_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "You need to enable permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }


    /**
     * In this method we have implemented the animation to the sidebar that moves
     * from register to login.
     *
     * @param view
     */
    public void continueRegisterToRegisterSidebarClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


}

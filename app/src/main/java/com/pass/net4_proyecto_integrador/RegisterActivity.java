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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

/**
 * This class provides functionality to the activity_register layout.
 * @version 1.0
 * @author Sebastian Huete, Sergio Turdasan, Alvaro Tunon y Pablo Diaz.
 */
public class RegisterActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSON_CODE = 100;
    private static final int REQUEST_IMAGE_GALLERY = 101;
    private Uri imageUri;
    private ImageView galleryImage;
    File fileImage;
    Bitmap bitmap;
    Context context = this;

    TextInputLayout name, email, phoneNumber, passwd;
    Button btnRegister;

    FirebaseDatabase myDatabase;
    DatabaseReference myDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        galleryImage = (ImageView) findViewById(R.id.image_add);

        name = findViewById(R.id.textInputName);
        email = findViewById(R.id.textInputEmail);
        phoneNumber = findViewById(R.id.textInputMobile);
        passwd = findViewById(R.id.textInputPassword);
        btnRegister = findViewById(R.id.cirRegisterButton);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabase = FirebaseDatabase.getInstance();
                myDatabaseReference = myDatabase.getReference("Users");

                String newName = name.getEditText().getText().toString();
                String newEmail = email.getEditText().getText().toString().replace(".", "1");
                String newPhoneNumber = phoneNumber.getEditText().getText().toString();
                String newPasswd = passwd.getEditText().getText().toString();

                User newUser = new User(newName, newEmail, newPhoneNumber, newPasswd);

                myDatabaseReference.child(newEmail).setValue(newUser);
            }
        });


        Glide.with(this)
                .load("android.resource://" + getPackageName() + "/"+ R.drawable.monster_interrogation_add_icon)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(galleryImage);

        galleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        openGallery();
                    }else{
                        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSON_CODE);
                    }
                }else{

                }

            }
        });
    }

    /**
     * this method is useful for get the Gallery Image
     * It have permissions
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY){
                imageUri = data.getData();
                Glide.with(this)
                        .load(imageUri)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade(300))
                        .circleCrop()
                        .into(galleryImage);
            }else{
                Log.i("RAG","Result: " + resultCode);
                Toast.makeText(this,"You did not choose any photo", Toast.LENGTH_SHORT).show();
            }

        super.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_PERMISSON_CODE){
            if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(this, "You need to enable permissions", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery(){
       Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }




    /**
     * In this method we have implemented the animation to the sidebar that moves
     * from register to login.
     * @param view
     */
    public void registerToLoginSidebarClick(View view){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

}

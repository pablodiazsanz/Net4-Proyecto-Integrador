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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        galleryImage = (ImageView) findViewById(R.id.image_add);

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

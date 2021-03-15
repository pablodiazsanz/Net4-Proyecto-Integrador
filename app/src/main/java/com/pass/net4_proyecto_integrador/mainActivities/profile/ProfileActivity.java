package com.pass.net4_proyecto_integrador.mainActivities.profile;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.CollectUserData;
import com.pass.net4_proyecto_integrador.SettingsActivity;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;
import com.pass.net4_proyecto_integrador.R;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.helpAlert.HelpAlertActivity;
import com.pass.net4_proyecto_integrador.mainActivities.notifications.ComunityActivity;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private BottomNavigationView bnv;
    //firebase
    private FirebaseAuth firebaseAuth;
   FirebaseUser user;
   FirebaseDatabase firebaseDatabase;
   DatabaseReference databaseReference;


    private TextView usernameTxtView, nameTxtView, emailTxtView, phoneNumberTxtView, descriptionTxtView;
    private ImageView img;
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private Map<String, String> userMap;
    private String email;
    private static final String USERS = "Users";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bnv = findViewById(R.id.nav_view_profile);
        bnv.setSelectedItemId(R.id.navigation_profile);
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
                        startActivity(new Intent(getApplicationContext(), HelpAlertActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_chat:
                        startActivity(new Intent(getApplicationContext(), ComunityActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.navigation_profile:
                        return true;
                }
                return false;
            }
        });

        //iniciar firebase
        /*firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot ds : snapshot.getChildren()){
                        String username = "" + ds.child("username").getValue();
                        String name = "" + ds.child("name").getValue();
                        String email = "" + ds.child("email").getValue();
                        String phone = "" + ds.child("phoneNumber").getValue();
                        String description = "" + ds.child("description").getValue();

                        usernameTxtView.setText(username);
                        nameTxtView.setText(name);
                        emailTxtView.setText(email);
                        phoneNumberTxtView.setText(phone);
                        descriptionTxtView.setText(description);

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS);
        Log.v("USERID", userRef.getKey());

        usernameTxtView = findViewById(R.id.textViewUserNameProfile);
        nameTxtView = findViewById(R.id.nameEditText);
        emailTxtView = findViewById(R.id.emailEditText);
        phoneNumberTxtView = findViewById(R.id.phoneEditText);
        descriptionTxtView = findViewById(R.id.descriptionEditText);

        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            String name, username, mail, phone, description;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyId: dataSnapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(email)) {
                        username = keyId.child("username").getValue(String.class);
                        name = keyId.child("name").getValue(String.class);
                        mail = keyId.child("email").getValue(String.class);
                        phone = keyId.child("phoneNumber").getValue(String.class);
                        description = keyId.child("description").getValue(String.class);
                        Log.v("username",username);
                        break;
                    }
                }
                usernameTxtView.setText(username);
                nameTxtView.setText(name);
                emailTxtView.setText(mail);
                phoneNumberTxtView.setText(phone);
                descriptionTxtView.setText(description);

    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }



    });


    }
}

package com.pass.net4_proyecto_integrador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pass.net4_proyecto_integrador.mainActivities.dashboard.DashboardActivity;
import com.pass.net4_proyecto_integrador.mainActivities.maps.MapsActivity;

/**
 * This class provides functionality to the activity_login layout.
 *
 * @author Sebastian Huete, Sergio Turdasan, Alvaro Tunon y Pablo Diaz.
 * @version 1.0
 */
public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView txt_forgot_password;
    TextInputLayout email, passwd;
    //Google
    private ImageView google_login;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "LoginActivity";
    private static FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 54654;
    //Facebook
    private CallbackManager callbackManager;
    private LoginButton facebook_login;
    private AccessTokenTracker accessTokenTracker;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Here we load the items of the layout
        btn_login = findViewById(R.id.btn_login);
        txt_forgot_password = findViewById(R.id.txt_forget);
        google_login = findViewById(R.id.google_login);
        facebook_login = findViewById(R.id.facebook_login);
        email = findViewById(R.id.textInputEmailLogin);
        passwd = findViewById(R.id.textInputPasswordLogin);
        //facebook_login.setBackgroundResource(R.drawable.ic_facebook);

        //Google & Facebook
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginButtonClick();
        forgotPasswordClick();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Configure Facebook Sign In
        facebook_login.setReadPermissions("email", "public_profile");
        facebookLoginClick();
        authListener();
        // Configure Facebook Listener
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            // Configure Facebook remove Listener
            mAuth.removeAuthStateListener(authStateListener);
            mAuth.signOut();
        }
    }

    /**
     * @return FirebaseAuth Icreated
     */
    public static FirebaseAuth recogerInstancia() {
        FirebaseAuth mAuth_creado = mAuth;
        return mAuth_creado;
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Here is the call back for Faccebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        //Here is the signed account of google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleResult(task);
        }
    }

    //****************** FACEBOOK ****************

    /**
     * This method is to create a Auth Listener
     */
    private void authListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "Usuario encontardo");
                }
            }
        };
    }

    /**
     * In this method we have put the setOnClickListener of the Facebook login LoginButton.
     * Here we check if the client register, cancel the operation or it was an error
     */
    private void facebookLoginClick() {
        facebook_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError");
            }
        });
    }

    /**
     * In this method check if the register was successful, if it was, it do the Intent and change to the mainactivity
     *
     * @param accessToken
     */
    private void handleFacebookResult(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Log In Successfully facebook", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Log In Failed Facebook", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //****************** GOOGLE ****************

    /**
     * In this method we have put the setOnClickListener of the Google login ImageView.
     */
    private void googleLoginClick() {
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /**
     * In this method we do an Itent to look for our google address
     */
    private void signIn() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    /**
     * Here we take the client account and result from the api
     *
     * @param task
     */
    private void handleGoogleResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Log In Successfully", Toast.LENGTH_LONG).show();
            firebaseGoogleAuth(account);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Log In Failed", Toast.LENGTH_LONG).show();
            firebaseGoogleAuth(null);
            e.printStackTrace();
        }
    }

    /**
     * in this method if the account is success it change to the mainActivity
     *
     * @param account
     */
    private void firebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    //FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }

    //****************** BUTTONS ACTIONS ****************

    /**
     * In this method we have put the setOnClickListener of the login button.
     */
    private void loginButtonClick() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });
    }


    private Boolean validateEmail() {
        String value = email.getEditText().getText().toString();

        if (value.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String value = passwd.getEditText().getText().toString();

        if (value.isEmpty()) {
            passwd.setError("Field cannot be empty");
            return false;
        } else {
            passwd.setError(null);
            passwd.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {
        //Validate login info
        if (!validateEmail() | !validatePassword()) {
            return;
        } else {
            userInDatabase();
        }
    }

    private void userInDatabase() {
        final String userEmail = email.getEditText().getText().toString().replace(".", "1").trim();
        final String userPasswd = passwd.getEditText().getText().toString().trim();

        DatabaseReference myDatabaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query checkUser = myDatabaseReference.orderByChild("email").equalTo(userEmail);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    email.setError(null);
                    email.setErrorEnabled(false);

                    Log.d("SNAPSHOTD", snapshot.toString());

                    String passwdFromDB = snapshot.child(userEmail).child("passwd").getValue(String.class);

                    if (passwdFromDB.equals(userPasswd)) {

                        passwd.setError(null);
                        passwd.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEmail).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userEmail).child("email").getValue(String.class);
                        String phoneNumberFromDB = snapshot.child(userEmail).child("phoneNumber").getValue(String.class);

                        Intent profileIntent = new Intent("SEND_PERSONAL_DATA");
                        profileIntent.putExtra("name", nameFromDB);
                        profileIntent.putExtra("email", emailFromDB);
                        profileIntent.putExtra("phoneNumber", phoneNumberFromDB);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(profileIntent);


                        Intent accessIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        accessIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        accessIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(accessIntent);

                    } else {
                        email.setError("No such email found");
                        email.requestFocus();
                    }
                } else {
                    passwd.setError("Wrong password");
                    passwd.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERRORE", error.getMessage());
            }
        });
    }


    /**
     * In this method we have implemented the setOnClickListener of the
     * forgot password text field.
     */
    private void forgotPasswordClick() {
        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogForgotPassword();
            }
        });
    }

    /**
     * In this method we have implemented the animation to the sidebar that moves
     * from login to register.
     *
     * @param view
     */
    public void loginToRegisterSidebarClick(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    /**
     * In this method, the password recovery screen has been implemented
     * as an alert dialog.
     */
    private void showAlertDialogForgotPassword() {
        // Here we setup the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(getLayoutInflater().inflate(R.layout.activity_email_recovery,
                null));

        // Here we create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize listener to track user signing in/out
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("LOGED IN", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("LOGID IN", "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    // Attach listener to FirebaseAuth
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // Remove listener from FirebaseAuth
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // When tapped on the register button
    public void register(View view) {

        // Initialize edit texts
        EditText register_email = (EditText) findViewById(R.id.register_username);
        EditText register_password = (EditText) findViewById(R.id.register_password);

        // Get user input
        email = register_email.getText().toString();
        password = register_password.getText().toString();

        // Give a warning when password is shorter than 6 characters
        if (password.length() < 6){
            Toast.makeText(SecondActivity.this, "Password too short!",
                    Toast.LENGTH_SHORT).show();
        }

        // If requirements are met create user and go to settings screen
        else {
            createUser();
        }
    }

    public void createUser() {
        // Create a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGED IN", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SecondActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SecondActivity.this, "Account created!",
                                    Toast.LENGTH_SHORT).show();

                            // If register succeeded go to settings screen
                            goToSettings();
                        }

                    }
                });
    }

    // Go to Log In screen
    public void goToLogin(View view) {
        Intent login = new Intent(this, ThirdActivity.class);
        startActivity(login);
    }

    // Go to settings screen
    public void goToSettings(){
        // Go to settings screen
        Intent settings = new Intent(this, FourthActivity.class);
        startActivity(settings);
    }
}

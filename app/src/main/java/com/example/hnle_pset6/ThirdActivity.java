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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThirdActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Initialize firebase authorization
        mAuth = FirebaseAuth.getInstance();

        // Initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize listener
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

    // Go to register screen
    public void goToRegister(View view) {
        Intent register = new Intent(this, SecondActivity.class);
        startActivity(register);
    }

    // When log in button is tapped
    public void login(View view) {
        // Initialize edit texts
        EditText loginEmail = (EditText) findViewById(R.id.login_username);
        EditText loginPassword = (EditText) findViewById(R.id.login_password);

        // Get user input
        email = loginEmail.getText().toString();
        password = loginPassword.getText().toString();

        // Log in user
        logInUser();
    }

    // Function to log in user
    public void logInUser(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGGED IN", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("LOGGED IN", "signInWithEmail:failed", task.getException());
                            Toast.makeText(ThirdActivity.this, "SIGN IN FAILED",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ThirdActivity.this, "Signed in",
                                    Toast.LENGTH_SHORT).show();

                            // If login succeeded go to core screen where the temperature is shown
                            goToFifth();
                        }
                    }
                });
    }

    // Go to the core screen where temperature is shown
    public void goToFifth(){
        Intent loggedIn = new Intent(this, FifthActivity.class);
        loggedIn.putExtra("ID", "ThirdActivity");
        startActivity(loggedIn);
    }
}

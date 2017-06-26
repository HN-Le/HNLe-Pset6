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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ThirdActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email;
    String password;
    String emailEncode;
    String userTemp;
    String userCity;

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

        // Give a warning when there is no mail filled in
        if (email.equals("")){
            Toast.makeText(ThirdActivity.this, "Invalid mail!",
                    Toast.LENGTH_SHORT).show();
        }

        // Give a warning when password is shorter than 6 characters
        if (password.length() < 6){
            Toast.makeText(ThirdActivity.this, "Password too short!",
                    Toast.LENGTH_SHORT).show();
        }

        else { logInUser(); }
    }

    // Function to log in user
    public void logInUser(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("LOGGED IN", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user
                        if (!task.isSuccessful()) {
                            Log.w("LOGGED IN", "signInWithEmail:failed", task.getException());
                            Toast.makeText(ThirdActivity.this, "Sign in failed! Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // If sign in succeeds
                        else {
                            Toast.makeText(ThirdActivity.this, "Signed in",
                                    Toast.LENGTH_SHORT).show();

                            // Encode string to find in database
                            emailEncode = EncodeString(email);

                            // Retrieve user preference
                            mDatabase.child("users").child(emailEncode).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.

                                    User user = dataSnapshot.getValue(User.class);
                                    userTemp = user.getTemp();
                                    userCity = user.getCity();

                                    if (userTemp != null) {
                                        WeatherAsyncTaskLogIn Asynctask = new WeatherAsyncTaskLogIn(ThirdActivity.this);
                                        Asynctask.execute(userCity);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("result", "Failed to read value.", error.toException());
                                }
                            });

                        }
                    }
                });
    }

    // Go to the core screen where temperature is shown
    public void goToFifth(String temp){
        Intent loggedIn = new Intent(this, FifthActivity.class);

        Bundle extra = new Bundle();
        extra.putString("ID", "ThirdActivity");
        extra.putString("temp", temp);

        loggedIn.putExtras(extra);
        startActivity(loggedIn);
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}

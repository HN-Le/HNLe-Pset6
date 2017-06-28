/*
 *  Hy Nhu Le (Tiny)
 *   11130717
 *
 *  The first screen a new user will see when the app is opened. Mainly used a visual teaser
 *   for the app. Shows a start button with the only purpose to go to the next screen.
 */

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userTemperature;
    private String userCity;
    private String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("USERS", "2");

                    retrieveExistingUser();

                }
                else {
                    Log.d("USERS", "NO USER");

                }
            }
        };

        Log.d("USERS", "1");

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    // When the button "Start" is tapped go to the next activity
    public void intro_button(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
        startActivity(register);
    }

    public String retrieveTemp(String temp){
        userTemperature = temp;
        return userTemperature;

    }

    public String retrieveCity(String temp){
        userCity = temp;
        return userCity;
    }

    public void goToFifth(){
        Intent intent = new Intent(this, WeatherActivity.class);
        Bundle extra = new Bundle();

        extra.putString("ID", "StartActivity");
        extra.putString("userCity", userCity);

        intent.putExtras(extra);

        startActivity(intent);
    }

    public void retrieveExistingUser(){
        final String uid = EncodeString(mAuth.getCurrentUser().getEmail());

        // Retrieve user preference
        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                User user = dataSnapshot.getValue(User.class);

                // Get user temperature
                userTemperature = user.getTemp();
                userCity = user.getCity();

                goToFifth();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StartActivity.this, "Database Error! ",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
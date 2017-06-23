package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.R.attr.start;

public class FifthActivity extends AppCompatActivity {


    // get user location

    // If temp => than user preference, set shirt visible and set to "Shirt" time TODO

    // Else, set jacket visible and set to "Jacket" time TODO

    // Change temp to actual temperature from API TODO

    String settings;
    String test;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        // Get intent
        Intent intent = this.getIntent();

        // As long as the intent exists
        if (intent != null){

            // See what previous activity was
            String activity = intent.getExtras().getString("ID");

            // If previous activity was the setting screen, get the new/adjusted temperature
            if (activity.equals("ThirdActivity")){
                Bundle extras = getIntent().getExtras();
                settings = extras.getString("setting");
            }
        }

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

                else {
                    // User is signed out
                    Log.d("LOGID IN", "onAuthStateChanged:signed_out");
                    goToLogInScreen();
                }
            }
        };

        final String uid = EncodeString(mAuth.getCurrentUser().getEmail());
        Log.d("result", "Email is: " + uid);


        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                User user = dataSnapshot.getValue(User.class);
                test = user.getTemp();
                Log.d("result", "User is: " + user);
                Log.d("result", "Test is: " + test);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("result", "Failed to read value.", error.toException());
            }
        });


    }


    // Renders the menu in the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // When + sign is pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        goToSettings();
        return true;
    }

    public void goToSettings(){
        Intent intent = new Intent(this, FourthActivity.class);
        startActivity(intent);
    }

    public void goToLogInScreen() {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
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

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}

package com.example.hnle_pset6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FourthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    String settingTemp;
    String settingCity;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

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

    public void done(View view) {
        // Save/update preferences in database
        saveInDatabase();
    }

    public void saveInDatabase(){

        // If user is logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
        {
            // Retrieve email from user in database
            userId = user.getEmail();

            // Get user input
            EditText settingsTshirt = (EditText) findViewById(R.id.setting_shirt_temp);
            EditText settingsCity = (EditText) findViewById(R.id.setting_city) ;
            settingTemp = settingsTshirt.getText().toString();
            settingCity = settingsCity.getText().toString();

            if (settingTemp.equals("")){
            Toast.makeText(FourthActivity.this, "Please fill in a temperature!",
                    Toast.LENGTH_SHORT).show();
            }

            if (settingsCity.equals("")){
                Toast.makeText(FourthActivity.this, "Please fill in a city!",
                        Toast.LENGTH_SHORT).show();
            }

            else {

            // Make a new User object and save the email and preferred temperature
            User userSettings = new User (userId, settingTemp, settingCity);

            // Replace "." with "," to put into database
            String encodeEmail = EncodeString(userId);

            // Put into database
            mDatabase.child("users").child(encodeEmail).setValue(userSettings);

            WeatherAsyncTaskRegister Asynctask = new WeatherAsyncTaskRegister(this);
            Asynctask.execute(settingCity);
            }

        }

        // If not logged in go to register screen
        else { goToRegisterScreen(); }
    }

    public void goToCoreScreen(String temp){

        Intent intent = new Intent(this, FifthActivity.class);
        Bundle extra = new Bundle();

        // ID activity
        extra.putString("ID", "FourthActivity");

        // User temperature preference
        extra.putString("setting", settingTemp);

        // Current temperature
        extra.putString("temperature", temp);

        intent.putExtras(extra);

        startActivity(intent);
    }

    public void goToLogInScreen() {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
    }

    public void goToRegisterScreen() {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    public static String DecodeString(String string) {
        return string.replace(",", ".");
    }

}


package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
        // save preferences in database TODO
        saveInDatabase();
        goToCoreScreen();
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

            // Make a new User object and save the email and preferred temperature
            User userSettings = new User (userId, settingTemp, settingCity);

            // Replace "." with "," to put into database
            String encodeEmail = EncodeString(userId);

            // Put into database
            mDatabase.child("users").child(encodeEmail).setValue(userSettings);

            WeatherAsyncTask Asynctask = new WeatherAsyncTask(this);
            Asynctask.execute(settingCity);
        }

        // If not logged in go to register screen
        else{
            goToRegisterScreen();
        }
    }

    public void goToCoreScreen(){
        Intent intent = new Intent(this, FifthActivity.class);
        intent.putExtra("setting", settingTemp);
        intent.putExtra("ID", "FourthActivity");
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

    public void goToCore2(ArrayList<String> movieData) {

        Intent intent = new Intent(this, FifthActivity.class);
        intent.putExtra("setting", settingTemp);
        intent.putExtra("ID", "FourthActivity");
        intent.putExtra("data", movieData);
        this.startActivity(intent);

    }
}


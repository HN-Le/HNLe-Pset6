/*
 *  Hy Nhu Le (Tiny)
 *   11130717
 *
 *   The fourth screen a new user sees or an existing user who wants to change its preference.
 *   In this screen the user can change the city and temperature.
*/

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String settingTemp;
    private String settingCity;
    private String userId;
    private String city;
    public String check;

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

                // If user is not signed in
                if (user == null) {
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

    // When pressed on the done button, save the preferences in the database
    public void done(View view) {
        // Save/update preferences in database
        saveInDatabase();
    }

    // Function to save user preferences in the database
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

            // Show warning if the temperature field is left blank
            if (settingTemp.equals("")){
            Toast.makeText(SettingsActivity.this, "Please fill in a temperature!",
                    Toast.LENGTH_SHORT).show();
            }

            // Show warning if the city field is left blank
            if (settingCity.equals("")){
                Toast.makeText(SettingsActivity.this, "Please fill in a city!",
                        Toast.LENGTH_SHORT).show();
            }

            // If everything is filled in, save it in database and get current temperature
            else { lookUpData(); }
        }

        // If not logged in go to register screen
        else { goToRegisterScreen(); }
    }

    // Go to the fifth screen where current temperature is shown
    public void goToWeather(String temp){

        Intent intent = new Intent(this, WeatherActivity.class);

        // Make a bundle to pass ID, user temperature and current temperature to the next screen
        Bundle extra = new Bundle();
        extra.putString("ID", "SettingsActivity");
        extra.putString("setting", settingTemp);
        extra.putString("temperature", temp);
        extra.putString("city", city);
        intent.putExtras(extra);

        // Go to next screen
        startActivity(intent);
    }

    // Function to go to the in log screen
    public void goToLogInScreen() {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    // Function to go to the register screen
    public void goToRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    // Function that turns "." into ","
    public static String encodeString(String string) {
        return string.replace(".", ",");
    }

    // A function to retrieve the current temperature from the async task
    public String retrieveTemp(String temp){
        check = temp;
        return check;
    }

    // A function to retrieve the city from the async task
    public String retrieveCity(String cityName){
        city = cityName;
        return city;
    }

    public void lookUpData(){

        // Use asynctask to get current temperature of the city the user
        WeatherAsyncTaskRegister Asynctask = new WeatherAsyncTaskRegister(this);
        Asynctask.execute(settingCity);

        // Give a warning if the city is an nonsense input
        if(retrieveTemp(check) == null){
            Toast.makeText(SettingsActivity.this, "Please fill in an existing city!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void saveData(){

        // Make a new User object and save the email and preferred temperature
        User userSettings = new User (userId, settingTemp, settingCity);

        // Replace "." with "," to put into database
        String encodeEmail = encodeString(userId);

        // Put into database
        mDatabase.child("users").child(encodeEmail).setValue(userSettings);
    }
}


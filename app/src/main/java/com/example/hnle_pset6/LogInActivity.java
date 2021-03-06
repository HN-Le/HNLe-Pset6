/*
 * Hy Nhu Le (Tiny)
 *  11130717
 *
 *  The third screen a new user sees or when an existing user was on the register screen and
 *  pressed the "already a member" button. In this screen the user can log in with their email
 *  and password or get redirected to the register screen by tapping the "Not a member yet?" button.
*/


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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;
    private String emailEncode;
    private String userTemp;
    private String userCity;
    public String city;
    String temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Initialize firebase authorization
        mAuth = FirebaseAuth.getInstance();

        // Initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Go to register screen
    public void goToRegister(View view) {
        Intent register = new Intent(this, RegisterActivity.class);
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

        // Give a warning when the email field is left blank
        if (email.equals("")){
            Toast.makeText(LogInActivity.this, "Invalid mail!",
                    Toast.LENGTH_SHORT).show();
        }

        // Give a warning when the password is shorter than 6 characters
        if (password.length() < 6){
            Toast.makeText(LogInActivity.this, "Password too short!",
                    Toast.LENGTH_SHORT).show();
        }

        // If everything is filled in try to log in the user
        else { logInUser(); }
    }

    // Function to log in user. Returns a message if it fails.
    public void logInUser(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user
                        if (!task.isSuccessful()) {
                            Toast.makeText(LogInActivity.this, "Failed to sign in!, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // If sign in succeeds
                        else {
                            Toast.makeText(LogInActivity.this, "Signed in",
                                    Toast.LENGTH_SHORT).show();

                            // Replace "." with "," to find email in database
                            emailEncode = encodeString(email);

                            // Retrieve user preference
                            mDatabase.child("users").child(emailEncode).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    User user = dataSnapshot.getValue(User.class);

                                    // Get the user temperature and city settings
                                    userTemp = user.getTemp();
                                    userCity = user.getCity();

                                    // If the user temperature is not null
                                    if (userTemp != null) {

                                        // Use asynctask to retrieve current temperature based on user city
                                        WeatherAsyncTaskLogIn Asynctask = new WeatherAsyncTaskLogIn(LogInActivity.this);
                                        Asynctask.execute(userCity);

                                    }
                                }

                                // Error check
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Toast.makeText(LogInActivity.this, "Database error!, Please try again.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
    }

    // Go to the fifth screen where current temperature is shown
    public void goToFifth(String temp){
        Intent loggedIn = new Intent(this, WeatherActivity.class);

        // Make a bundle to pass ID and current temperature to the next screen
        Bundle extra = new Bundle();
        extra.putString("ID", "LogInActivity");
        extra.putString("temp", temp);
        extra.putString("city", city);
        loggedIn.putExtras(extra);

        // Go to next screen
        startActivity(loggedIn);
    }

    // Function that turns "." into ","
    public static String encodeString(String string) { return string.replace(".", ","); }

    // Get the city from the asynctask. In case a user misspells a city and the API returns the
    // closest looking city the textview in fifth activity will still display the city of the API
    // and not the misspelled city of user.
    public String retrieveCity(String cityName){
        city = cityName;
        return city;
    }

    public String retrieveTemp(String temp){
        temperature = temp;
        return temperature;

    }
}

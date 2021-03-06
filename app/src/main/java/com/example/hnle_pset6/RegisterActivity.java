/*
 * Hy Nhu Le (Tiny)
 *  11130717
 *
 * The second screen a new user sees or when an existing user signed out and pressed the
 *  "Not registered yet?" button. In this screen the user can register for an account with their
 *  email. Or go to the login screen by pressing the "Already a member" button that redirects the
 *  user to the log in screen.
 */

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // Initialize the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
    }

    // When tapped on the register button
    public void register(View view) {

        // Initialize edit texts
        EditText register_email = (EditText) findViewById(R.id.register_username);
        EditText register_password = (EditText) findViewById(R.id.register_password);

        // Get user input
        email = register_email.getText().toString();
        password = register_password.getText().toString();

        // Give a warning when the email field is left blank
        if (email.equals("")){
            Toast.makeText(RegisterActivity.this, "Invalid mail!",
                    Toast.LENGTH_SHORT).show();
        }

        // Give a warning when password is shorter than 6 characters
        if (password.length() < 6){
            Toast.makeText(RegisterActivity.this, "Password too short!",
                    Toast.LENGTH_SHORT).show();
        }

        // If requirements are met create user and go to settings screen
        else { createUser(); }
    }

    // Function to create a new user. Returns a message if it fails.
    public void createUser() {
        // Create a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Cannot make account! Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // If sign in succeeds
                        else {
                            Toast.makeText(RegisterActivity.this, "Account created!",
                                    Toast.LENGTH_SHORT).show();

                            // If register succeeded go to settings screen for setup
                            goToSettings();
                        }
                    }
                });
    }

    // Go to log in screen
    public void goToLogin(View view) {
        Intent login = new Intent(this, LogInActivity.class);
        startActivity(login);
    }

    // Go to settings screen
    public void goToSettings(){
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }
}

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class FifthActivity extends AppCompatActivity {

    public String settings;
    private String temperatureString;
    private String test_string;
    private String city;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);

        // Initialize firebase authorization
        mAuth = FirebaseAuth.getInstance();

        // Initialize firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // User is signed in
                if (user != null) {
                }

                // If user is not signed in
                else {
                    // Go back to log in screen
                    goToLogInScreen();
                }
            }
        };

        // Get current user
        final String uid = EncodeString(mAuth.getCurrentUser().getEmail());

        // Retrieve user preference
        mDatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated
                User user = dataSnapshot.getValue(User.class);

                // Get user temperature
                test_string = user.getTemp();

                // If the user temperature is not null
                if (test_string != null) {
                    show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FifthActivity.this, "Database Error! ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = this.getIntent();

        // As long as the intent exists
        if (intent != null) {

            // See what previous activity was
            String activity = intent.getExtras().getString("ID");

            // If previous activity was the setting screen, get the new/adjusted temperature
            if (activity.equals("FourthActivity")) {

                Bundle extras = getIntent().getExtras();

                temperatureString = extras.getString("temperature");
                settings = extras.getString("setting");
                search = extras.getString("search");


            }

            // If previous activity was the log in screen, get the current temperature
            else if (activity.equals("ThirdActivity")){

                Bundle extras = getIntent().getExtras();
                temperatureString = extras.getString("temp");
                search = extras.getString("search");

            }
        }
    }

    @Override
    public void onBackPressed() {
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
        int id = item.getItemId();

        if (id == R.id.settings){
            goToSettings();
            return true;
        }

        if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void show(){

        // Show temperature
        TextView show_temperature = (TextView) findViewById(R.id.show_temp);
        show_temperature.setText(temperatureString + " ℃");

        // Cast to float
        float temperature = Float.parseFloat(temperatureString);
        float test = Float.parseFloat(test_string);

        // Set visibility shirt/jacket and text
        ImageView shirt = (ImageView) findViewById(R.id.show_img_shirt);
        ImageView jacket = (ImageView) findViewById(R.id.show_img_jacket);
        TextView userText = (TextView) findViewById(R.id.show_txt);
        TextView showCity = (TextView) findViewById(R.id.show_city);

        showCity.setText(search);

        if (temperature <= test) {
            jacket.setVisibility(View.VISIBLE);
            shirt.setVisibility(View.INVISIBLE);
            userText.setText("Jacket");
        }

        else {
            shirt.setVisibility(View.VISIBLE);
            jacket.setVisibility(View.INVISIBLE);
            userText.setText("Shirt");
        }

    }
}

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

public class FifthActivity extends AppCompatActivity {

    String settings;
    String temperature_string;
    String test_string;
    String city;
    FifthActivity fifthAct;
    float test;
    float temperature;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

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
                if (user != null) {
                    // User is signed in
                    Log.d("LOGED IN", "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d("LOGID IN", "onAuthStateChanged:signed_out");
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
                // whenever data at this location is updated.

                User user = dataSnapshot.getValue(User.class);
                test_string = user.getTemp();
                city = user.getCity();


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

                temperature_string = extras.getString("temperature");
                settings = extras.getString("setting");
            }

            // If previous activity was the log in screen, get the current temperature
            else if (activity.equals("ThirdActivity")){

                Bundle extras = getIntent().getExtras();

                temperature_string = extras.getString("temp");
            }
        }
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

    public void show(){

        // Show temperature
        TextView show_temperature = (TextView) findViewById(R.id.show_temp);
        show_temperature.setText(temperature_string + " ℃");

        // Cast to float
        temperature = Float.parseFloat(temperature_string);
        test = Float.parseFloat(test_string);

        // Set visibility shirt/jacket and text
        ImageView shirt = (ImageView) findViewById(R.id.show_img_shirt);
        ImageView jacket = (ImageView) findViewById(R.id.show_img_jacket);
        TextView userText = (TextView) findViewById(R.id.show_txt);
        TextView userCity = (TextView) findViewById(R.id.show_city);



        userCity.setText(city.toUpperCase());

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

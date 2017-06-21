package com.example.hnle_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class FifthActivity extends AppCompatActivity {

    String settings;

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


        // get user location

        // If temp => than user preference, set shirt visible and set to "Shirt" time TODO

        // Else, set jacket visible and set to "Jacket" time TODO

        // Change temp to actual temperature from API TODO
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





}

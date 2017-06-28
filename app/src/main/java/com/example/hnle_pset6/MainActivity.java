/*
    The first screen a new user will see when the app is opened. Mainly used a visual teaser
    for the app. Shows a start button with the only purpose to go to the next screen.

 */

package com.example.hnle_pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // When the button "Start" is tapped go to the next activity
    public void intro_button(View view) {
        Intent register = new Intent(this, SecondActivity.class);
        startActivity(register);
    }
}
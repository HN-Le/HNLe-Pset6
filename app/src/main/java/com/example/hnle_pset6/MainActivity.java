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

        // If already logged in go to fifth activity TODO

        // If not first use but logged out go to login screen

        // If not go to register screen TODO

    }

    public void intro_button(View view) {
        Intent register = new Intent(this, SecondActivity.class);
        startActivity(register);
    }

    // Alleen showen waneer het de eerste keer is! TODO
}
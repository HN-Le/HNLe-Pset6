package com.example.hnle_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherAsyncTask extends AsyncTask<String, Integer, String> {
    Context context;
    JSONArray temperature;
    FourthActivity fourthAct;

    public WeatherAsyncTask(FourthActivity fourth){
        this.fourthAct = fourth;
        this.context = this.fourthAct.getApplicationContext();
    }

    // Before app searches in API
    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Retrieving data", Toast.LENGTH_SHORT).show();
    }

    // Searching in API
    @Override
    protected String doInBackground(String... params) {
        return HttpRequestHelper.downloadFromServer(params);
    }

    // After retrieving data
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        ArrayList<String> data = new ArrayList<>();

        try {

            JSONObject movieStreamObject = new JSONObject(result);

            temperature = movieStreamObject.optJSONArray("Main");

            if (temperature != null) {

                String title;

                // loop through JSON object and get all the info
                for (int i = 0; i < temperature.length(); i++) {

                    JSONObject object = temperature.getJSONObject(i);

                }

                this.fourthAct.goToCore2(data);
            }

            // if input is nonsense and no search results are available
            else{
                Toast.makeText(context, "Invalid title! Please try again!", Toast.LENGTH_SHORT).show();
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

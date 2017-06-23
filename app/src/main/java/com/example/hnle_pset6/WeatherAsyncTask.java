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
    String temperature;
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

        try {

            JSONObject temperatureStreamObject = new JSONObject(result);
            JSONObject temperatureObj = temperatureStreamObject.getJSONObject("main");

            temperature = temperatureObj.getString("temp");
            Log.d("result", temperature);
        }

        catch(JSONException e){
                e.printStackTrace();
            }

            this.fourthAct.goToCoreScreen(temperature);
    }
}

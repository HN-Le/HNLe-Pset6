package com.example.hnle_pset6;

import android.content.Context;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WeatherAsyncTaskLogIn extends AsyncTask<String, Integer, String> {
    Context context;
    String temperature_string;
    ThirdActivity thirdAct;

    public WeatherAsyncTaskLogIn(ThirdActivity third){
        this.thirdAct = third;
        this.context = this.thirdAct.getApplicationContext();
    }

    // Before app searches in API
    @Override
    protected void onPreExecute() {
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
            temperature_string = temperatureObj.getString("temp");

        }

        catch(JSONException e){
            e.printStackTrace();
        }

        this.thirdAct.goToFifth(temperature_string);
    }
}

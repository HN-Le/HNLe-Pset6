

//  SETTINGS PAGE

package com.example.hnle_pset6;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WeatherAsyncTaskRegister extends AsyncTask<String, Integer, String> {
    Context context;

    String temperature;
    FourthActivity fourthAct;
    String search;

    public WeatherAsyncTaskRegister(FourthActivity fourth){
        this.fourthAct = fourth;
        this.context = this.fourthAct.getApplicationContext();
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
            JSONObject searchObj = new JSONObject(result);
            search = searchObj.getString("name");

            JSONObject temperatureObj = temperatureStreamObject.getJSONObject("main");
            temperature = temperatureObj.getString("temp");

        }

            catch (JSONException e) { e.printStackTrace(); }

        this.fourthAct.check(temperature);

        if (this.fourthAct.check != null){

            this.fourthAct.retrieveSearch(search);
            this.fourthAct.goToFifthScreen(temperature);
        }

    }
}

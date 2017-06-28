/*
    Asynctask to retrieve the city and temperature in the API for new users
*/

package com.example.hnle_pset6;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAsyncTaskRegister extends AsyncTask<String, Integer, String> {
    Context context;

    String temperature;
    FourthActivity fourthAct;
    String city;

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
            // Save city and temperature data in a string
            JSONObject temperatureStreamObject = new JSONObject(result);
            JSONObject searchObj = new JSONObject(result);
            city = searchObj.getString("name");

            JSONObject temperatureObj = temperatureStreamObject.getJSONObject("main");
            temperature = temperatureObj.getString("temp");

        }

            catch (JSONException e) { e.printStackTrace(); }

        // Sent temperature back to fourth activity
        this.fourthAct.retrieveTemp(temperature);

        // Only sent the city and temperature back if it exists
        if (this.fourthAct.check != null){

            this.fourthAct.retrieveCity(city);
            this.fourthAct.goToFifthScreen(temperature);
        }

    }
}

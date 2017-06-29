/*
 *  Hy Nhu Le (Tiny)
 *   11130717
 *
 *   Asynctask to retrieve the city and temperature in the API for new users
*/

package com.example.hnle_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAsyncTaskRegister extends AsyncTask<String, Integer, String> {
    private Context context;

    private String temperature;
    private SettingsActivity settingsAct;
    public String city;

    public WeatherAsyncTaskRegister(SettingsActivity fourth){
        this.settingsAct = fourth;
        this.context = this.settingsAct.getApplicationContext();
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
        this.settingsAct.retrieveTemp(temperature);

        // Only sent the city and temperature back if it exists
        if (!TextUtils.isEmpty(city)){
            this.settingsAct.retrieveCity(city);
            this.settingsAct.saveData();
            this.settingsAct.goToWeather(temperature);
        }
    }
}

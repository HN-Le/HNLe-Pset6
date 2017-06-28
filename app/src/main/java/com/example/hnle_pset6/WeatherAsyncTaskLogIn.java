/*
    Asynctask to retrieve the city and temperature in the API for existing users
*/

package com.example.hnle_pset6;

import android.content.Context;
import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAsyncTaskLogIn extends AsyncTask<String, Integer, String> {
    private Context context;
    private String temperatureString;
    private ThirdActivity thirdAct;
    public String city;

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
            // Save city and temperature data in a string
            JSONObject temperatureStreamObject = new JSONObject(result);
            JSONObject searchObj = new JSONObject(result);
            city = searchObj.getString("name");

            JSONObject temperatureObj = temperatureStreamObject.getJSONObject("main");
            temperatureString = temperatureObj.getString("temp");

        }

        catch(JSONException e){
            e.printStackTrace();
        }

        // Sent back the city data
        this.thirdAct.retrieveCity(city);

        // Go to the fifth screen and sent the temperature data to the login activity
        this.thirdAct.goToFifth(temperatureString);
    }


}

/*
 *  Hy Nhu Le (Tiny)
 *   11130717
 *
 *   Asynctask to retrieve the city and temperature in the API for existing users
*/

package com.example.hnle_pset6;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherAsyncTaskExistingUser extends AsyncTask<String, Integer, String> {
    private Context context;
    private String temperatureString;
    private WeatherActivity weather;
    public String city;

    public WeatherAsyncTaskExistingUser(WeatherActivity weather){
        this.weather = weather;
        this.context = this.weather.getApplicationContext();
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

            Log.d("VOLGORDE", "1");
//            this.start.retrieveExistingUser();

            Log.d("VOLGORDE", "2");

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
        this.weather.retrieveCity(city);
        Log.d("TEMPERATUUR", city);


        this.weather.retrieveTemp(temperatureString);

        Log.d("TEMPERATUUR", temperatureString);


        // Go to the fifth screen and sent the temperature data to the login activity
//        this.start.goToFifth(temperatureString);
    }


}

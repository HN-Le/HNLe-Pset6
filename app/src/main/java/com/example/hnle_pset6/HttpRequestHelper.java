/*
    Helper class to download data from API
*/

package com.example.hnle_pset6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpRequestHelper {

    protected static synchronized String downloadFromServer(String... params) {
        String result = "";

        // the city name from user input
        String chosenTag = params[0];

        URL site = null;
        String url;
        try {
            url = "http://api.openweathermap.org/data/2.5/weather?q=" + chosenTag + ",nl&units=metric&APPID=0627785f144d64903f9f797cc9a1e945" ;
            site = new URL(url);
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection connect;

        // Download from API
        try {
            // Initialize
            connect = (HttpURLConnection) site.openConnection();
            connect.setRequestMethod("GET");

            // Save response code, 200 is good more than 300 is bad
            Integer responseCode = connect.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {

                BufferedReader bReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String line;
                while ((line = bReader.readLine()) != null) {

                    result += line;
                }
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

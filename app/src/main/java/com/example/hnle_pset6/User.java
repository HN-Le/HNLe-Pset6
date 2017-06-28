/*
 *  Hy Nhu Le (Tiny)
 *  11130717
 *
 *   Object class to store user and user preferences
*/

package com.example.hnle_pset6;

public class User {
    private String userId;
    private String temp;
    private String city;

    // Default constructor for firebase
    public User() {
    }

    // Constructor to make objects
    public User(String name, String temperature, String cities) {
        this.userId = name;
        this.temp = temperature;
        this.city = cities;
    }

    // Get temperature from object
    public String getTemp() {
        return temp;
    }

    // Get city from object
    public String getCity() {
        return city;
    }

}

package com.example.hnle_pset6;

public class User {
    private String userEmail;
    private int temp;

    // Default constructor for firebase
    public User() {}

    // Constructor to make objects
    public User(String name, int temperature){
        this.userEmail = name;
        this.temp = temperature;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}

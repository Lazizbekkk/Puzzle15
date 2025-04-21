package com.example.puzzle15;


import android.content.SharedPreferences;

public class Repository {

    private Repository() {
    }

    private static Repository instance;
    static SharedPreferences sharedPref;

    static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }
    static void setSharedPref(SharedPreferences sharedPreferences) {
        sharedPref = sharedPreferences;
    }

    static SharedPreferences getSharedPref() {
        return sharedPref;
    }

}

package com.example.puzzle15;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

public class MyApp extends Application {
   static  Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Repository.setSharedPref(this.getSharedPreferences("Lazizbek", Context.MODE_PRIVATE));
    }
}

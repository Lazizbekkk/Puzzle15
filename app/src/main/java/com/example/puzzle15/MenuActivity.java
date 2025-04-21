package com.example.puzzle15;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    public static boolean continueBtnClicked;
    private Button continueBtn;
    private Button playBtn;
    private Button infoBtn;
    private Button exitBtn;


    private SharedPreferences sharedPreferences = Repository.getSharedPref();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menyu);
        boolean playClicked = sharedPreferences.getBoolean("aaa", false);


        Log.d("LLL", "Menu On OnCreate");
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        initView();

        Log.d("LLL",playBtn.getText().toString());





        if (playClicked) {
            Log.d("LLL", String.valueOf(playClicked) +"ishladi");
            playBtn.setText("NEW GAME");
            continueBtn.setVisibility(View.VISIBLE);
        } else {
            Log.d("LLL", String.valueOf(playClicked) +"ishlamadi");
            playBtn.setText("PLAY");
            continueBtn.setVisibility(View.GONE);
        }

//        if(sharedPreferences.getBoolean("isWin",false)){
//            Log.d("LLL", String.valueOf(playClicked) +"ishladi");
//            playBtn.setText("Play");
//            continueBtn.setVisibility(View.GONE);
//            sharedPreferences.edit().putBoolean("isWin",false).apply();
//        } else {
//            Log.d("LLL", String.valueOf(playClicked) +"ishlamadi");
//            playBtn.setText("New");
//            continueBtn.setVisibility(View.GONE);
//        }



        continueBtn.setOnClickListener(v -> {
            continueBtnClicked = true;
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        });


        playBtn.setOnClickListener(v -> {

            if (playBtn.getText().equals("PLAY")) {
                Log.d("LLL", "PlayBtn if ishladi...");
                sharedPreferences.edit().putBoolean("aaa", true).apply();
            }
            else{
                Log.d("LLL", "PlayBtn if ishlamadi...");
            }

            sharedPreferences.edit()
                    .remove("chronometer")
                    .remove("stopTime")
                    .putInt("count", 0)
                    .putLong("chronometer", SystemClock.elapsedRealtime())
                    .putBoolean("win_restart", true)
                    .putBoolean("new_game", true)
                    .apply();
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        });

        infoBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        });

        exitBtn.setOnClickListener(v -> {
            finishAffinity();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }





    private void initView() {
        playBtn = findViewById(R.id.play_btn);
        continueBtn = findViewById(R.id.continue_btn_menu);
        exitBtn = findViewById(R.id.exit_btn_menu);
        infoBtn = findViewById(R.id.info_btn_menu);

    }


}
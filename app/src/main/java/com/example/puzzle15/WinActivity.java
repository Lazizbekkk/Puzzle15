package com.example.puzzle15;



import static com.example.puzzle15.Utils.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class WinActivity extends AppCompatActivity {
    private int record1;
    private int record2;
    private int record3;
    private TextView recordBtn1;
    private TextView recordBtn2;
    private TextView recordBtn3;
    public SharedPreferences sharedPreferences = Repository.getSharedPref();


    @SuppressLint({"SetTextI18n", "MissingInflatedId", "CutPasteId", "CommitPrefEdits"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        EdgeToEdge.enable(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        recordBtn1 = findViewById(R.id.record1);
        recordBtn2 = findViewById(R.id.record2);
        recordBtn3 = findViewById(R.id.record3);

        record1 = sharedPreferences.getInt("record1", 0);
        record2 = sharedPreferences.getInt("record2", 0);
        record3 = sharedPreferences.getInt("record3", 0);

        Log.d("record","-----------------");
        Log.d("record","onCreate holatida");
        Log.d("record","record1 = " + record1);
        Log.d("record","record2 = " + record2);
        Log.d("record","record3 = " + record3);
        Log.d("record","-----------------");

        int record = getIntent().getIntExtra("record", 0);



        Log.d("record", "count keldi -> " + record);


        findViewById(R.id.win_restart).setOnClickListener(v -> {
//            sharedPreferences.edit()
//                        .remove(CHRONOMETER)
//                    .remove("stopTime")
//                    .remove("count")
//                    .putLong(CHRONOMETER, SystemClock.elapsedRealtime())
//                    .apply();
            Log.d("WIN_RESTART_BTN"," WIN_RESTART_BTN oldingi windan oldi --->   " + sharedPreferences.getBoolean(WIN_RESTART_BTN,false));
            sharedPreferences.edit().putBoolean(WIN_RESTART_BTN,true).apply();
            Log.d("WIN_RESTART_BTN"," WIN_RESTART_BTN keyingi windan ketdi --->   " + sharedPreferences.getBoolean(WIN_RESTART_BTN,false));

            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.win_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("SetTextI18n")
    private void recordSort(int record) {
        int []arr = sortRecord(record1,record2,record3,record);
        int count = 0;
        for (int j : arr) {
            if (j == 0) count++;
        }

        if(count == 3){
            record1 = record;
            record2 = 0;
            record3 = 0;
        }
        else if (count == 2) {
            record1 = arr[2];
            record2 = arr[3];
            record3 = 0;
        }
        else if (count == 1) {
            record1 = arr[1];
            record2 = arr[2];
            record3 = arr[3];
        }
        else {
            record1 = arr[0];
            record2 = arr[1];
            record3 = arr[2];
        }

        sharedPreferences.edit()
                .putInt("record1", record1)
                .putInt("record2", record2)
                .putInt("record3", record3)
                .apply();
        Log.d("record","--------------------------");
        Log.d("record","recordSort methodi ishladi");



        recordBtn1.setText(String.valueOf(record1));
        recordBtn2.setText(String.valueOf(record2));
        recordBtn3.setText(String.valueOf(record3));
        if(record1 == 0){
            recordBtn1.setText("----");
        }
        if(record2 == 0){
            recordBtn2.setText("----");
        }
        if(record3 == 0){
            recordBtn3.setText("----");
        }

    }



    private int[] sortRecord(int record1, int record2, int record3, int record){
        int[] records = new int[4];
        records[0] = record1;
        records[1] = record2;
        records[2] = record3;
        records[3] = record;
        Arrays.sort(records);
        Log.d("record", "-------   " + Arrays.toString(records) + "    ---------");
        Log.d("record","record1 = " + record1);
        Log.d("record","record2 = " + record2);
        Log.d("record","record3 = " + record3);
        return records;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("record","onResumeBuldi");
        recordSort(getIntent().getIntExtra("record", 0));
//        record1 = sharedPreferences.getInt("record1", record1);
//        record2 = sharedPreferences.getInt("record2", record2);
//        record3 = sharedPreferences.getInt("record3", record3);
//        recordBtn1.setText(String.valueOf(record1));
//        recordBtn2.setText(String.valueOf(record2));
//        recordBtn3.setText(String.valueOf(record3));
        Log.d("record","record1 = " + record1);
        Log.d("record","record2 = " + record2);
        Log.d("record","record3 = " + record3);
        Log.d("record","-----------------");
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.edit()
                .putInt("record1", record1)
                .putInt("record2", record2)
                .putInt("record3", record3)
                .apply();

        Log.d("record","onPause buldi");
        Log.d("record","record1 = " + record1);
        Log.d("record","record2 = " + record2);
        Log.d("record","record3 = " + record3);
        Log.d("record","-----------------");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this,MenuActivity.class);
//        startActivity(intent);
//    }

}
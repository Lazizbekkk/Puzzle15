package com.example.puzzle15;


import static com.example.puzzle15.Utils.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.shapes.ArcShape;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class PlayActivity extends AppCompatActivity {
    public static boolean continueState;
    private Chronometer chronometer;
    private TextView tvScore;
    private int count;
    private boolean homeBtn;
    private Kordinatalar emptySpace;
    private TextView record1;
    private TextView record2;
    private TextView record3;
    private StringBuilder stringBuilder;
    private Button[][] buttons = new Button[4][4];
    private ArrayList<String> values;
    public SharedPreferences sharedPreferences = Repository.getSharedPref();
    private int x, y;
    private boolean newGameBtnClicked = false;
    private ImageView imgButtonSounds;

    private boolean sound = sharedPreferences.getBoolean(SOUNDS, true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Log.d("STATE222", "-----------------------------------");
        Log.d("STATE222", "onCreate");
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);


        initView();
        initData();
        loadData();

        if (sound) {
            soundOn();
        } else {
            soundOf();
        }


        if (sharedPreferences.getBoolean("aaa", false)) {
            restart();
            Log.d("FFF", "play clicked hozir ishladi:  " + count);
        }





        findViewById(R.id.restart).setOnClickListener(v -> {
            restart();
        });

        findViewById(R.id.cup_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, WinActivity.class);
            startActivity(intent);
        });
        chronometer = findViewById(R.id.chronometer1);


        findViewById(R.id.home_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuActivity.class);
            homeBtn = true;
            startActivity(intent);
        });


    }

    private void initView() {
        imgButtonSounds = findViewById(R.id.btnSong);

        record1 = findViewById(R.id.record1);
        record2 = findViewById(R.id.record2);
        record3 = findViewById(R.id.record3);
        chronometer = findViewById(R.id.chronometer1);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        tvScore = findViewById(R.id.countbutton);


        ConstraintLayout puzzleButtons = findViewById(R.id.puzzle15btns);

        imgButtonSounds.setOnClickListener(view -> {
            sound = !sound;
            if (sound) {
                soundOn();
            } else {
                soundOf();
            }

            sharedPreferences.edit().putBoolean(SOUNDS, sound).apply();
        });


        for (int i = 0; i < puzzleButtons.getChildCount(); i++) {
            Button button = (Button) puzzleButtons.getChildAt(i);
            int currentX = i / 4;
            int currentY = i % 4;
            buttons[currentX][currentY] = button;
            button.setOnClickListener(this::onClick);
            button.setTag(new Kordinatalar(currentX, currentY));

        }

        findViewById(R.id.restart).setOnClickListener((View v) -> restart());
        tvScore.setOnLongClickListener(view -> {
                    values = new ArrayList<>();
                    setVisibleButtons();
                    for (int i = 1; i <= 16; i++) {
                        values.add(String.valueOf(i));
                    }
                    for (int i = 0; i < 16; i++) {
                        if (values.get(i).equals("16")) {
                            x = i / 4;
                            y = i % 4;
                            buttons[i / 4][i % 4].setText("");
                            buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
                            continue;
                        }

                        buttons[i / 4][i % 4].setText(values.get(i));
                    }
                    return true;
                }

        );
    }

    private void initData() {
        values = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            values.add(String.valueOf(i));
        }

        do {
            Collections.shuffle(values);
        } while (!isSolvable(values,4));

    }

    private void loadData() {
        shuffle();


        Log.d("WWW", "stringBuilder.toString():  " + stringBuilder);

        for (int i = 0; i < 16; i++) {
            if (values.get(i).equals("16")) {
                x = i / 4;
                y = i % 4;
                buttons[i / 4][i % 4].setText("");
                buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
                continue;
            }

            buttons[i / 4][i % 4].setText(values.get(i));
        }

        Log.d("WWW", "stringBuilder.toString(): 2222:::: " + stringBuilder);
    }


    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                final Window window = getWindow();
                window.setDecorFitsSystemWindows(false);
                final WindowInsetsController controller = window.getInsetsController();
                if (controller != null) {
                    controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            } else {
                hideSystemUI();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onStart() {
        super.onStart();

        boolean soundOn = sharedPreferences.getBoolean(SOUNDS, true);


        homeBtn = false;
        Log.d("STATE222", "onStart");
        count = sharedPreferences.getInt("count", 0);
        tvScore.setText("step: " + sharedPreferences.getInt("count", 0));
        String[] matrixGetText = sharedPreferences.getString("buttonTextSaved", getStringBuilder()).split("#");
        x = sharedPreferences.getInt("spaceX", 3);
        y = sharedPreferences.getInt("spaceY", 3);


        for (int i = 0; i < 16; i++) {
            if (!matrixGetText[i].equals("16")) {
                buttons[i / 4][i % 4].setText(matrixGetText[i]);
                buttons[i / 4][i % 4].setVisibility(View.VISIBLE);
            } else {
                buttons[i / 4][i % 4].setText("");
                buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
            }
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {

        super.onResume();
        if (sound) {
            soundOn();
        } else {
            soundOf();
        }


        Log.d("STATE222", "onResume");
        long stopTime = sharedPreferences.getLong("stopTime", SystemClock.elapsedRealtime());
        long savedTime = sharedPreferences.getLong("chronometer", SystemClock.elapsedRealtime());
        chronometer.setBase(savedTime + SystemClock.elapsedRealtime() - stopTime);
        chronometer.start();
        count = sharedPreferences.getInt("count", 0);
        tvScore.setText("step: " + sharedPreferences.getInt("count", 0));
        String[] matrixGetText = sharedPreferences.getString("buttonTextSaved", getStringBuilder()).split("#");
        x = sharedPreferences.getInt("spaceX", 3);
        y = sharedPreferences.getInt("spaceY", 3);
        for (int i = 0; i < 16; i++) {
            if (!matrixGetText[i].equals("16")) {
                buttons[i / 4][i % 4].setText(matrixGetText[i]);
                buttons[i / 4][i % 4].setVisibility(View.VISIBLE);
            } else {
                buttons[i / 4][i % 4].setText("");
                buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
            }
        }
        if (getIntent().getBooleanExtra("win_restart", false)) {
            restart();
        }
        newGameBtnClicked  = sharedPreferences.getBoolean("new_game",false);
        if (newGameBtnClicked) {
            sharedPreferences.edit().putBoolean("new_game",false).apply();
            newGameBtnClicked = false;
            restart();
        }
        if(sharedPreferences.getBoolean(WIN_RESTART_BTN,false)){
            Log.d("WIN_RESTART_BTN"," WIN_RESTART_BTN oldingi playdan oldi --->   " + sharedPreferences.getBoolean(WIN_RESTART_BTN,false));
            sharedPreferences.edit().putBoolean(WIN_RESTART_BTN,false).apply();
            Log.d("WIN_RESTART_BTN"," WIN_RESTART_BTN keyingi windan ketdi --->   " + sharedPreferences.getBoolean(WIN_RESTART_BTN,false));
            restart();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d("STATE222", "onPause");

        MusicManager.pauseMusic();


        StringBuilder buttonTextSavedShared = new StringBuilder();
        int spaceX = 3;
        int spaceY = 3;
        boolean continueState = false;


        if (count != 0) continueState = true;
        for (int i = 0; i < 16; i++) {
            if (!buttons[i / 4][i % 4].getText().equals("")) {
                buttonTextSavedShared.append(buttons[i / 4][i % 4].getText().toString()).append("#");
            } else {
                buttonTextSavedShared.append("16").append("#");
                spaceY = i % 4;
                spaceX = i / 4;
            }
        }

        sharedPreferences.edit().
                putInt("count", count).putLong("chronometer", chronometer.getBase())
                .putLong("stopTime", SystemClock.elapsedRealtime())
                .putString("buttonTextSaved", buttonTextSavedShared.toString())
                .putInt("spaceX", spaceX).putInt("spaceY", spaceY)
                .putBoolean("continueState", continueState).apply();
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onStop() {
        super.onStop();
        MusicManager.pauseMusic();

        Log.d("STATE222", "onStop");

        StringBuilder buttonTextSavedShared = new StringBuilder();
        int spaceX = 3;
        int spaceY = 3;
        boolean continueState = false;
        if (count != 0) continueState = true;
        for (int i = 0; i < 16; i++) {
            if (!buttons[i / 4][i % 4].getText().equals("")) {
                buttonTextSavedShared.append(buttons[i / 4][i % 4].getText().toString()).append("#");
            } else {
                buttonTextSavedShared.append("16").append("#");
                spaceY = i % 4;
                spaceX = i / 4;
            }
        }

        sharedPreferences.edit().
                putInt("count", count)
                .putString("buttonTextSaved", buttonTextSavedShared.toString())
                .putInt("spaceX", spaceX).putInt("spaceY", spaceY)
                .putBoolean("continueState", continueState).apply();
    }


    void setVisibleButtons() {
        for (int i = 0; i < 16; i++) {
            buttons[i / 4][i % 4].setVisibility(View.VISIBLE);
        }
    }


    private void shuffle() {
        initData();
        setVisibleButtons();

    }

    private void onClick(View view) {
        Button clikedButton = (Button) view;
        Kordinatalar kordinatalar = (Kordinatalar) clikedButton.getTag();

        Button emptyBtn = buttons[x][y];

        int delX = Math.abs(x - kordinatalar.getX());
        int delY = Math.abs(y - kordinatalar.getY());

        if (delX + delY == 1) {
            emptyBtn.setText(clikedButton.getText());
            emptyBtn.setVisibility(View.VISIBLE);

            clikedButton.setText("");
            clikedButton.setVisibility(View.INVISIBLE);

            x = kordinatalar.getX();
            y = kordinatalar.getY();

            count++;
            continueState = true;
            if (x == 3 && y == 3) {
                isChecked();
            }
        }
        TextView textView = findViewById(R.id.countbutton);
        textView.setText("step: " + count);

    }

    private void isChecked() {
        ConstraintLayout relativeLayout = findViewById(R.id.puzzle15btns);
        for (int i = 1; i < 16; i++) {
            Button button = (Button) relativeLayout.getChildAt(i - 1);
            if (!button.getText().equals(String.valueOf(i))) {
                return;
            }
        }
        sharedPreferences.edit().putBoolean(YOU_WIN,true).apply();
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra("record", count);
        sharedPreferences.edit().putBoolean("aaa",false).apply();
        Log.d("Record", "cout ketdi -> " + count);
        startActivity(intent);
        Log.d("TTT", "Ishladi: isChecked()");
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    @SuppressLint("SetTextI18n")
    public void restart() {
        Log.d("WWW", "restart ishladi");
        loadData();
        count = 0;
        TextView textView = findViewById(R.id.countbutton);
        textView.setText("step: " + count);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public String getStringBuilder() {
        stringBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            stringBuilder.append(values.get(i)).append('#');
        }
        return stringBuilder.toString();
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(this, MenuActivity.class);
        homeBtn = true;
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chronometer.setBase(savedInstanceState.getLong("chronometer"));
        chronometer.start();
        count = savedInstanceState.getInt("count", 0);
        tvScore.setText("step: " + count);
        ArrayList<String> listButtonGetText = savedInstanceState.getStringArrayList("buttonTextSaved");
        boolean[] buttonGetVisibility = savedInstanceState.getBooleanArray("buttonVisibilitySaved");
        x = savedInstanceState.getInt("spaceX", 3);
        y = savedInstanceState.getInt("spaceY", 3);
        for (int i = 0; i < 16; i++) {
            assert listButtonGetText != null;
            if (!listButtonGetText.get(i).equals("16")) {
                buttons[i / 4][i % 4].setText(listButtonGetText.get(i));
                assert buttonGetVisibility != null;
                if (buttonGetVisibility[i]) {
                    buttons[i / 4][i % 4].setVisibility(View.VISIBLE);
                }
            } else {
                buttons[i / 4][i % 4].setText("");
                buttons[i / 4][i % 4].setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("count", count);
        outState.putLong("chronometer", chronometer.getBase());
        ArrayList<String> buttonTextSaved = new ArrayList<>();
        boolean[] buttonVisibilitySaved = new boolean[16];
        int spaceX = 3;
        int spaceY = 3;

        for (int i = 0; i < 16; i++) {
            if (!buttons[i / 4][i % 4].getText().equals("")) {
                buttonTextSaved.add(buttons[i / 4][i % 4].getText().toString());
                if (buttons[i / 4][i % 4].getVisibility() == View.VISIBLE) {
                    buttonVisibilitySaved[i] = true;
                }
            } else {
                buttonTextSaved.add("16");
                spaceY = i % 4;
                spaceX = i / 4;
            }
        }
        outState.putStringArrayList("buttonTextSaved", buttonTextSaved);
        outState.putBooleanArray("buttonVisibilitySaved", buttonVisibilitySaved);
        outState.putInt("spaceX", spaceX);
        outState.putInt("spaceY", spaceY);
        Log.d("LLL", buttonTextSaved.toString());
    }
    private boolean isSolvable(List<String> puzzle, int n) {
        int inversions = 0;
        int size = puzzle.size();
        int blankRow = -1;

        // Inversionlar sonini hisoblash
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (stringToInt(puzzle.get(i)) > stringToInt(puzzle.get(j)) && stringToInt(puzzle.get(i)) != 0 && stringToInt(puzzle.get(j)) != 0) {
                    inversions++;
                }
            }
            // Bo'sh joy qatorini topish
            if (stringToInt(puzzle.get(i)) == 0) {
                blankRow = i / n;  // Bo'sh joyning qator raqami
            }
        }

        // Bo'sh joy oxirgi qatorni hisobga oladigan joyda bo'lsa
        if (n % 2 == 0) {
            // Kvadrat o'lchami juft bo'lganda bo'sh joyning qatori teskari o'lchovda bo'lsa, inversion soni ta'sir qiladi
            if ((n - blankRow) % 2 == 0) {
                return inversions % 2 == 1; // Agar bo'sh joy oxirgi qator bo'lsa, inversiyalar soni toq bo'lishi kerak
            } else {
                return inversions % 2 == 0; // Boshqa qatorda bo'lsa, inversiyalar juft bo'lishi kerak
            }
        } else {
            return inversions % 2 == 0; // Kvadrat o'lchami toq bo'lganda faqat inversiyalar juft bo'lishi kerak
        }
    }

    private int stringToInt(String s) {
        int value = 0;
        for (int i = 0; i < s.length(); i++) {
            value = value * 10 + s.charAt(i) - '0';
        }
        return value;
    }




    private void soundOn() {
        MusicManager.startMusic();
        Log.d("KKK","soundOn");
        imgButtonSounds.setImageResource(R.drawable.ic_sound_on);
    }

    private void soundOf() {
        MusicManager.pauseMusic();
        Log.d("KKK","soundOf");
        imgButtonSounds.setImageResource(R.drawable.ic_sound_mute);
    }

}

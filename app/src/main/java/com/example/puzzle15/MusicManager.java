package com.example.puzzle15;

import android.media.MediaPlayer;

public class MusicManager {
    private static MusicManager musicManager;
    private static MediaPlayer mediaPlayer;
    static {
        mediaPlayer = MediaPlayer.create(MyApp.context, R.raw.game_media);
        mediaPlayer.setLooping(true);
    }

    static void startMusic() {
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    static void pauseMusic() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    static void stopMusic() {
        mediaPlayer.stop();
    }

    boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}

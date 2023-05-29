package com.example.myfinalproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.Provider;

public class MyService extends Service {
    private MediaPlayer mMediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
//        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mission_impossible);
        mMediaPlayer.setLooping(true);
    }

    public void onStart(Intent intent, int startId){
//        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        mMediaPlayer.start();
    }

    public void onDestroy(){
//        Toast.makeText(this, "Service Stopped", Toast.LENGTH_SHORT).show();
        mMediaPlayer.stop();
    }



}

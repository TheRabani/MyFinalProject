package com.example.myfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
        startService(serviceIntent);
//        getSupportActionBar().hide();
        Dialog dialog = new Dialog(this, R.style.dialogstyle);
        dialog.setContentView(R.layout.dialog_krav);
        dialog.show();

        TextView textView = findViewById(R.id.kravTXT);
        textView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_krav));
        TextView textView2 = findViewById(R.id.kravTXT2);
        textView2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.thenew));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 7500);

    }
}
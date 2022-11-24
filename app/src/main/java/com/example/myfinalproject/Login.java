package com.example.myfinalproject;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view) {
        mAuth = FirebaseAuth.getInstance();


//        Intent intent = new Intent(Login.this, MainActivity.class);
//        startActivity(intent);

    }

    public void GoToSignUp(View view) {
        startActivity(new Intent(Login.this, SignUp.class));
    }
}
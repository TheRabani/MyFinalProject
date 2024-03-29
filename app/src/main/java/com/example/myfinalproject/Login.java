package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText phone, otp, pass;
    Button login_btn;
    String verificationID;
    ProgressBar bar;
    public static boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        login_btn = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        bar = findViewById(R.id.bar);

    }

    private void sendverificationcode(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+972" + phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login.this, "verification failed", Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(Login.this, "Code sent", Toast.LENGTH_SHORT).show();
            bar.setVisibility(View.INVISIBLE);
            otp.setVisibility(View.VISIBLE);
            login_btn.setText("Verify");
        }
    };

    private void verifycode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
        signinbycredentials(credential);
    }

    private void signinbycredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        bar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, MainActivity.class));
                        } else {
                            Toast.makeText(Login.this, "Wrong code!", Toast.LENGTH_SHORT).show();
                            shake(R.id.otp);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            if(currentUser.getUid().equals("qnaTTq2SfmMnPb1KjRPNBxYsZEh1"))
                isAdmin = true;
//            mAuth.signOut();
        }
    }

    public void GoLogin(View view) {

        if (login_btn.getText().toString().toLowerCase().equals("gen otp")) {
            if (TextUtils.isEmpty(phone.getText().toString()) || phone.getText().toString().length() != 10) {
                Toast.makeText(Login.this, "enter valid phone nm.", Toast.LENGTH_SHORT).show();
                shake(R.id.phone);
            } else {
                String number = phone.getText().toString();
                bar.setVisibility(View.VISIBLE);
                sendverificationcode(number);
            }
//            bar.setVisibility(View.VISIBLE);
        } else {
            if (TextUtils.isEmpty(otp.getText().toString())) {
                Toast.makeText(Login.this, "enter valid nm.", Toast.LENGTH_SHORT).show();
                shake(R.id.otp);
            } else {
                bar.setVisibility(View.VISIBLE);
                verifycode(otp.getText().toString());
            }
        }

    }

    private void shake(int id) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(findViewById(id));
    }
}
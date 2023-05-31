package com.example.myfinalproject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ShopFragment shopFragment = new ShopFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    private String current;
    FragmentTransaction transaction;
    public Fragment currentFragment = new HomeFragment();
    private Intent serviceIntent;
    AudioManager audioManager;
    public Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(getApplicationContext(), MyService.class);
//        startService(serviceIntent);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

//        bottomNavigationView = findViewById(R.id.bottom_navigation);
        chipNavigationBar = findViewById(R.id.bottom_navigation);
        if (!Login.isAdmin) {
//        if (true) {
//        chipNavigationBar.setBackground(R.drawable.show_gun_background);
//        chipNavigationBar.setBackgroundColor(Color.WHITE);
            chipNavigationBar.setItemSelected(R.id.home, true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit(); --------------------------
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new HomeFragment()).commit();
            current = "home";
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//        bottomNavigationView.setSelectedItemId(R.id.home);
            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    switch (i) {
                        case R.id.home:
                            fragment = new HomeFragment();
                            if (!current.equals("home")) {
                                if (current.equals("shop")) {
                                    transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                                    transaction.replace(R.id.container, fragment);
                                } else if (current.equals("cal")) {
//                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                                    transaction.replace(R.id.container, fragment);
                                }
                                transaction.addToBackStack(null);
                                transaction.commit();
                                current = "home";
                                currentFragment = fragment;
                            }
                            break;
                        case R.id.shop:
                            fragment = new ShopFragment();
                            if (!current.equals("shop") && current.equals("home") || current.equals("cal")) {
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                                transaction.replace(R.id.container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                current = "shop";
                                currentFragment = fragment;
                            }
                            break;
                        case R.id.calendar:
                            fragment = new CalendarFragment();
                            if (!current.equals("cal") && current.equals("home") || current.equals("shop")) {
                                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                                transaction.replace(R.id.container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                current = "cal";
                                currentFragment = fragment;
                            }
                            break;
                    }
//                if (fragment != null) {
//                    transaction.replace(R.id.container, fragment).commit();
//                }
                }
            });
        } else {
            chipNavigationBar.setMenuResource(R.menu.admin_bottom_nav_menu);
            chipNavigationBar.setItemSelected(R.id.adminCalendar, true);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new admin_fragment_schedule()).commit();
            current = "calendar";
            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    switch (i) {
                        case R.id.adminShop:
                            fragment = new admin_fragment_add_gun();
                            if (!current.equals("add")) {
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
                                transaction.replace(R.id.container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                current = "shop";
                            }
                            break;
                        case R.id.adminCalendar:
                            fragment = new admin_fragment_schedule();
                            if (!current.equals("calendar")) {
                                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                                transaction.replace(R.id.container, fragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                                current = "calendar";
                            }
                    }
                }
            });
        }


//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.home:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//                        return true;
//                    case R.id.shop:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, shopFragment).commit();
//                        return true;
//                    case R.id.calendar:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
//                        return true;
//                }
//                return false;
//            }
//        });
        findViewById(R.id.sett).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder tempBuilder = new AlertDialog.Builder(MainActivity.this);
                View tempDialogView = getLayoutInflater().inflate(R.layout.volume_bar, null, false);
                tempBuilder.setView(tempDialogView);
                AlertDialog tempAd = tempBuilder.create();
                tempAd.setCancelable(true);
                Window window = tempAd.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.TOP | Gravity.END;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
                tempAd.show();
                tempAd.getWindow().setLayout(850, 165);
                SeekBar seekBar = tempAd.findViewById(R.id.seekBar);
                if(seekBar == null)
                    Toast.makeText(MainActivity.this, "null", Toast.LENGTH_SHORT).show();
                seekBar.setMax(maxVol);
                seekBar.setProgress(currentVol);

                aSwitch = tempAd.findViewById(R.id.volSwitch);
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(!isChecked)
                            stopService(serviceIntent);
                        else
                            startService(serviceIntent);
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!isFinishing()) {
            stopService(serviceIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(serviceIntent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

    public void doit(View view) {
        Toast.makeText(this, "fewds", Toast.LENGTH_SHORT).show();
    }

}
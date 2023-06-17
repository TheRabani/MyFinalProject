package com.example.myfinalproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ShakeDetector.OnShakeListener{

    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;
    private String current;
    FragmentTransaction transaction;
    public Fragment currentFragment = new HomeFragment();
    private Intent serviceIntent;
    AudioManager audioManager;
    public Switch aSwitch;
    ShakeDetector shakeDetector;
    private final static int REQUEST_CODE=100;
    FusedLocationProviderClient fusedLocationProviderClient;
    public static List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(getApplicationContext(), MyService.class);
//        startService(serviceIntent);

        shakeDetector = new ShakeDetector(this);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(shakeDetector, acc, SensorManager.SENSOR_DELAY_NORMAL);

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(MainActivity.this);
        getLastLocation();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Calendar");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren())
                {
                    String key = ""+s.getKey();
                    if(HomeFragment.book_date.contains(""+key))
                        HomeFragment.deleteFromArrayLists(""+key);
                    if(!isDateAfter(""+key))
                        databaseReference.child(""+key).removeValue();
                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });




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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void doit(View view) {
        Toast.makeText(this, "fewds", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShakeDetected() {
//        Toast.makeText(this, "shake", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLastLocation()
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null)
                            {
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }
        else
            askPermission();
    }
    private void askPermission()
    {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getLastLocation();
            else
                Toast.makeText(MainActivity.this, "Required Permission", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static boolean isDateAfter(String dateString) {
        try {
            // Get today's date
            Calendar today = Calendar.getInstance();

            // Parse the given date string
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = dateFormat.parse(dateString);

            // Set the parsed date to 00:00:00 time
            Calendar givenDate = Calendar.getInstance();
            givenDate.setTime(date);
            givenDate.set(Calendar.HOUR_OF_DAY, 0);
            givenDate.set(Calendar.MINUTE, 0);
            givenDate.set(Calendar.SECOND, 0);
            givenDate.set(Calendar.MILLISECOND, 0);

            // Compare the given date with today's date
            return givenDate.after(today);
        } catch (ParseException e) {
            // Handle parsing errors
            e.printStackTrace();
            return false;
        }
    }
}
package com.example.myfinalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ShopFragment shopFragment = new ShopFragment();
    CalendarFragment calendarFragment = new CalendarFragment();
    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bottomNavigationView = findViewById(R.id.bottom_navigation);
        chipNavigationBar  = findViewById(R.id.bottom_navigation);
//        chipNavigationBar.setBackground(R.drawable.show_gun_background);

//        chipNavigationBar.setBackgroundColor(Color.WHITE);

        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
//        bottomNavigationView.setSelectedItemId(R.id.home);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.shop:
                        fragment = new ShopFragment();
                        break;
                    case R.id.calendar:
                        fragment = new CalendarFragment();
                        break;

                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

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

    }
}
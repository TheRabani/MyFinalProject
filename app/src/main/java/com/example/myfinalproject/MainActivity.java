package com.example.myfinalproject;

import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
    private String current;
    FragmentTransaction transaction;
    public Fragment currentFragment = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        bottomNavigationView = findViewById(R.id.bottom_navigation);
        chipNavigationBar = findViewById(R.id.bottom_navigation);
//        if (!Login.isAdmin) {
        if (true) {
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
            chipNavigationBar.setItemSelected(R.id.adminShop, true);
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new admin_fragment_add_gun()).commit();
            current = "add";
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

    }
}
package com.example.myfinalproject;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SelectListener{
    FirebaseAuth mAuth;
    Button logout, addGun;
    Button buttonGoTo, buttonNavigation;
    View view;
    Fragment fragment;
    RecyclerView recyclerView;
    public static ArrayList<String> book_id = new ArrayList<>(), book_date = new ArrayList<>(), book_time = new ArrayList<>();
    MyDatabaseHelper myDB;
    CustomAdapterSQLite customAdapterSQLite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = new MapFragment();
        view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        myDB = new MyDatabaseHelper(getContext());
        if (book_date.size() == 0) {
            storeDataInArrays();
        }
        customAdapterSQLite = new CustomAdapterSQLite(getContext(), book_id, book_date, book_time, this);
        recyclerView.setAdapter(customAdapterSQLite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        buttonNavigation = view.findViewById(R.id.buttonNavigation);
        buttonNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_navigation, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();
                ad.show();

                dialogView.findViewById(R.id.btnWaze).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            // Launch Waze to look for Hawaii:
                            String url = "https://waze.com/ul?q=מטווח קרב&navigate=yes";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            // If Waze is not installed, open it in Google Play:
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                            startActivity(intent);
                        }
                    }
                });

                dialogView.findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("geo:" + 31.752167930459038 + "," + 35.21662087865303));
                        Intent chooser = Intent.createChooser(intent, "Lunch Maps");
                        startActivity(chooser);

                    }
                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });


        addGun = (Button) view.findViewById(R.id.addGun);

        addGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), addGun.class));
            }
        });

        return view;
    }

    void storeDataInArrays() {
        myDB = new MyDatabaseHelper(getContext());
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0)
        {
            ImageView image = view.findViewById(R.id.imageNoData);
            image.setVisibility(View.VISIBLE);
        }
        else
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_time.add(cursor.getString(2));
            }
    }

    @Override
    public void onItemClicked(Schedule schedule) {
    }

    @Override
    public void onItemClicked(String string) {
//        Toast.makeText(getContext(), ""+string, Toast.LENGTH_SHORT).show();

        Toast.makeText(getContext(), ""+book_id, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_schedule, null, false);
        builder.setView(dialogView);
        AlertDialog ad = builder.create();

        Button yes = dialogView.findViewById(R.id.buttonDeleteYes);
        Button no = dialogView.findViewById(R.id.buttonDeleteNo);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (string != null && !string.equals("")) {
                    myDB.deleteOneRow(string);
                    ad.dismiss();
                    restoreDataInArrays();
                    deleteFromArrayLists(string);
                }
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });

        ad.show();
    }

    private void deleteFromArrayLists(String date) {
        book_id.remove(date);
        book_date.remove(date);
        book_time.remove(date);
    }

    void restoreDataInArrays() {
        myDB = new MyDatabaseHelper(getContext());
        Cursor cursor = myDB.readAllData();
        book_id.clear();
        book_date.clear();
        book_time.clear();
        if (cursor.getCount() == 0)
        {
            ImageView image = view.findViewById(R.id.imageNoData);
            image.setVisibility(View.VISIBLE);
        }
        else
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_time.add(cursor.getString(2));
            }
        customAdapterSQLite.notifyDataSetChanged();
    }
}
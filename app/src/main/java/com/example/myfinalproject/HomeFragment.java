package com.example.myfinalproject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    Button logout, addGun;
    Button buttonGoTo, buttonNavigation;
    View view;
    Fragment fragment;

//    public static FirebaseFirestore firestore;
//    public static gunAdapter adapter;
//    public static ArrayList<Gun> gunArrryList;
//    public static ListView gunListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = new MapFragment();
        view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonNavigation = view.findViewById(R.id.buttonNavigation);
        buttonNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_navigation, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();
                ad.show();

//                view = inflater.inflate(R.layout.fragment_home, container, false);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();

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
        logout = (Button) view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                if (currentUser != null) {
                    mAuth.signOut();
                    startActivity(new Intent(getActivity(), Login.class));
//                }
//                else
//                    startActivity(new Intent(getActivity(), Login.class));
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

}
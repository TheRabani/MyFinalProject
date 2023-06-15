package com.example.myfinalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class notification_activity extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "feefdfwdfdwfdw", Toast.LENGTH_SHORT).show();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout2, HomeFragment.fragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_notification, container, false);

        Toast.makeText(getActivity(), "feefdfwdfdwfdw", Toast.LENGTH_SHORT).show();
        getChildFragmentManager().beginTransaction().replace(R.id.frame_layout2, HomeFragment.fragment).commit();


        return view;
    }
}
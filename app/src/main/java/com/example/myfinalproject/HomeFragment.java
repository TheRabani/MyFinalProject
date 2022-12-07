package com.example.myfinalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {
    FirebaseAuth mAuth;
    Button logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        logout = view.findViewById()

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onClickBtn(View view) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
        {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
        }
        else
            startActivity(new Intent(getActivity(), Login.class));
    }
}
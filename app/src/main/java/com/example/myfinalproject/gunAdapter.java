package com.example.myfinalproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class gunAdapter extends ArrayAdapter<Gun> {

    private Context context;
    private ArrayList<Gun> gunArrayList;

    public gunAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Gun> gunArrayList) {
        super(context, resource, gunArrayList);

        this.context = context;
        this.gunArrayList = gunArrayList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.gun_row, null, false);
        Gun gun = gunArrayList.get(position);

        TextView tvGunName = view.findViewById(R.id.gunName);
        ImageView tvImage = view.findViewById(R.id.gunImage);

        tvGunName.setText(""+gun.getManufacturer()+" "+gun.getModelName());
//        tvImage.setImageResource();
//
        Picasso.get()
                        .load(""+gun.getImgUrl())
                                .into(tvImage);
//        Glide.with(view.getRootView()).load(""+gun.getImgUrl());
//        Glide.with(context).load(gun.getImgUrl());
//        Glide.with(view.getContext()).load(""+gun.getImgUrl());

        return view;
    }
}

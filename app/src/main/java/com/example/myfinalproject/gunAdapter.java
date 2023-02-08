package com.example.myfinalproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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

        String st = "" + gun.getManufacturer() + " " + gun.getModelName();

        tvGunName.setText(st);

        Node<GunBitMap> node = ShopFragment.nodeGunBitMap;
        Bitmap bitmap = ShopFragment.getBitmapFromName(st, node);
        if(bitmap != null)
            tvImage.setImageBitmap(bitmap);
        else
            tvImage.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.x));

//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/" + st);
//        try {
//            File localFile = File.createTempFile("" + st, "jpeg");
//
//            storageReference.getFile(localFile)
//                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                tvImage.setImageBitmap(bitmap);
//                            } else {
//                            }
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        tvImage.setImageResource();
//
//        Picasso.get()
//                        .load(""+gun.getImgUrl())
//                                .into(tvImage);
//        Glide.with(view.getRootView()).load(""+gun.getImgUrl());
//        Glide.with(context).load(gun.getImgUrl());
//        Glide.with(view.getContext()).load(""+gun.getImgUrl());

        return view;
    }
}

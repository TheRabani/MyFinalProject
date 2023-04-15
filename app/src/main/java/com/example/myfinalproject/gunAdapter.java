package com.example.myfinalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gunAdapter extends RecyclerView.Adapter<gunAdapter.gunHolder> {

    private Context context;
    private ArrayList<Gun> gunArrayList;
    private SelectListener listener;

    public gunAdapter(Context context, ArrayList<Gun> gunArrayList, SelectListener listener) {
        this.context = context;
        this.gunArrayList = gunArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public gunAdapter.gunHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gun_row, parent, false);
        return new gunHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gunAdapter.gunHolder holder, int position) {
        Gun gun = gunArrayList.get(position);
        holder.setDetails(gun);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onItemLongClicked(gun);
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(gun);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gunArrayList.size();
    }

    class gunHolder extends RecyclerView.ViewHolder {

        private TextView gunName;
        private ImageView imageView;

        public gunHolder(@NonNull View itemView) {
            super(itemView);
            gunName = itemView.findViewById(R.id.gunName);
            imageView = itemView.findViewById(R.id.gunImage);
        }

        void setDetails(Gun gun) {
            String st = "" + gun.getManufacturer() + " " + gun.getModelName();
            gunName.setText(st);
            Bitmap bitmap = ShopFragment.getBitmapFromName(st);
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.x));
        }
    }
    
    public void shake(View dialogView, int id) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(dialogView.findViewById(id));
    }
}
/*public class gunAdapter extends ArrayAdapter<Gun> {

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
*/
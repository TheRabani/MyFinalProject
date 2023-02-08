package com.example.myfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.meta.When;

public class ShopFragment extends Fragment implements EventListener<QuerySnapshot> {

    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    int countBit = 0;

    public DocumentSnapshot doc2;
    public String st;

    public GunBitMap[] gunBitMap;

    public int count = 0;
    Bitmap bitmap[];
    int howMany=0;

    private ListView gunListView;
    private gunAdapter adapter;

    ImageView tvImage;

    private ArrayList<Gun> gunArrryList;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = view.findViewById(R.id.btnAdd);
        tvImage = view.findViewById(R.id.gunImage);

        Gun lastSelected;

        gunArrryList = new ArrayList<Gun>();
        gunListView = view.findViewById(R.id.listViewGun);
        adapter = new gunAdapter(getActivity(), R.layout.gun_row, gunArrryList);
        gunListView.setAdapter(adapter);



//        -------------------------------------------------------------------------------------------------------------------
        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(getActivity());
        View tempDialogView = getLayoutInflater().inflate(R.layout.dialog_loading, null, false);
        tempBuilder.setView(tempDialogView);
        AlertDialog tempAd = tempBuilder.create();
//        tempAd.setCancelable(false);
        tempAd.show();



        Runnable mRunnable;
        Handler mHandler=new Handler();
        mRunnable=new Runnable() {
            @Override
            public void run() {
                tempAd.cancel();
            }
        };
        mHandler.postDelayed(mRunnable,15*1000);//Execute after 10 Seconds


        gunListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gun g = adapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_gun_details, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();

                ImageView imageView = dialogView.findViewById(R.id.imageGun);
                TextView makeAndModel = dialogView.findViewById(R.id.makeAndModel);
                TextView unitsInStock = dialogView.findViewById(R.id.unitsInStock);
                TextView magOptions = dialogView.findViewById(R.id.magOptions);
                TextView caliber = dialogView.findViewById(R.id.caliber);
                TextView weight = dialogView.findViewById(R.id.weight);
                TextView price = dialogView.findViewById(R.id.price);
                Button request = dialogView.findViewById(R.id.request);

                makeAndModel.setText("" + g.getManufacturer() + " " + g.getModelName());
                int num = g.getInStock();
                if(num!=0)
                    unitsInStock.setText("" + g.getInStock());
                else {
                    unitsInStock.setText("0");
                    request.setVisibility(View.VISIBLE);
                }

                request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                magOptions.setText("" + g.getOptionsMagCapacity());
                caliber.setText("" + g.getCaliber());
                weight.setText("" + g.getWeight());
                price.setText("" + g.getPrice());
//                Picasso.get()
//                        .load("" + g.getImgUrl())
//                        .into(imageView);
                ad.show();
            }
        });



        firestore
                .collection("guns")
                .addSnapshotListener(this);

//        firestore.collection("guns")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<DocumentSnapshot> docList = task.getResult().getDocuments();
//                            gunArrryList.clear();
//                            howMany = docList.size();
//                            count = -1;
//                            bitmap = new Bitmap[howMany];
//                            for (int i=0; i<bitmap.length; i++)
//                            {
//                                bitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.x);
//                            }
//                            for (DocumentSnapshot doc : docList) {
//                                count++;
//                                Gun gun = new Gun(
//                                        doc.getString("modelName"),
//                                        doc.getString("manufacturer"),
////                                              doc.getString("imgUrl"),
//                                        Integer.parseInt(doc.get("price").toString()),
//                                        Integer.parseInt(doc.get("inStock").toString()),
////                                        Integer.parseInt(doc.get("standardMagCapacity").toString()),
//                                        doc.getString("optionsMagCapacity"),
//                                        doc.getString("caliber"),
//                                        Integer.parseInt(doc.get("weight").toString())
////                                        Integer.parseInt(doc.get("barrelLength").toString()),
////                                        Integer.parseInt(doc.get("triggerPull").toString())
//                                );
//                                gunArrryList.add(gun);
//
//                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/"+ doc.getString("manufacturer") + " "+ doc.getString("modelName"));
//                                try {
//                                    File localFile = File.createTempFile(doc.getString("manufacturer") + " "+ doc.getString("modelName"), "jpeg");
//
//                                    storageReference.getFile(localFile)
//                                            .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                                                    if (task.isSuccessful()) {
//                                                        Bitmap tempBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
////                                                        tvImage.setImageBitmap(bitmap);
//                                                        bitmap[count] = tempBitmap;
//                                                    } else {
//                                                    }
//                                                }
//                                            });
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                            adapter.notifyDataSetChanged();
//                        } else
//                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });


        return view;
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        List<DocumentSnapshot> docList = value.getDocuments();
        gunArrryList.clear();
        if (docList.toArray().length != 0) {
            count = -1;
            howMany = docList.size();
//            bitmap = new Bitmap[howMany];
            gunBitMap= new GunBitMap[howMany];
            countBit = 0;
            for (int i=0; i<gunBitMap.length; i++)
            {
//                bitmap[i] = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.x);
            }
            for (DocumentSnapshot doc : docList) {

                count++;
                Gun gun = new Gun(
                        doc.getString("modelName"),
                        doc.getString("manufacturer"),
//                                              doc.getString("imgUrl"),
                        Integer.parseInt(doc.get("price").toString()),
                        Integer.parseInt(doc.get("inStock").toString()),
//                                        Integer.parseInt(doc.get("standardMagCapacity").toString()),
                        doc.getString("optionsMagCapacity"),
                        doc.getString("caliber"),
                        Integer.parseInt(doc.get("weight").toString())
//                                        Integer.parseInt(doc.get("barrelLength").toString()),
//                                        Integer.parseInt(doc.get("triggerPull").toString())
                );
                gunArrryList.add(gun);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/" + doc.getString("manufacturer") + " " + doc.getString("modelName"));
                try {
                    File localFile = File.createTempFile(doc.getString("manufacturer") + " " + doc.getString("modelName"), "jpeg");

                    storageReference.getFile(localFile)
                            .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Bitmap tempBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                                        tvImage.setImageBitmap(bitmap);
//                                        bitmap[count] = tempBitmap;
                                        gunBitMap[countBit] = new GunBitMap(doc.getString("manufacturer") + " " + doc.getString("modelName"), );
                                        countBit++;
                                    } else {
                                    }
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            adapter.notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}
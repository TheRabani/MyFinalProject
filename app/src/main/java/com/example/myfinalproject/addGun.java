package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class addGun extends AppCompatActivity implements EventListener<QuerySnapshot> {
    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    private ListView gunListView;
    private gunAdapter adapter;

    private ArrayList<Gun> gunArrryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gun);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = findViewById(R.id.btnAdd);

        gunArrryList = new ArrayList<Gun>();
        gunListView = findViewById(R.id.listViewGun);
        adapter = new gunAdapter(this, R.layout.gun_row, gunArrryList);


        gunListView.setAdapter(adapter);
        gunListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                DatabaseReference rootDatabaseReference = FirebaseDatabase.getInstance().getReference().child("guns");

                AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_gun, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();
                ad.show();
                Button buttonYes = dialogView.findViewById(R.id.buttonYes);
                Button buttonNo = dialogView.findViewById(R.id.buttonNo);

                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        firestore.collection("guns")
//                                .document("1670921447982").delete();
                        ad.dismiss();
                    }
                });

                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });

//                startActivity(new Intent(addGun.this, MainActivity.class));
                return false;
            }
        });

        firestore
                .collection("guns")
                .addSnapshotListener(this);
        firestore.collection("guns")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> docList = task.getResult().getDocuments();
                            gunArrryList.clear();
                            for (DocumentSnapshot doc : docList) {
                                Gun gun = new Gun(
                                        doc.getString("modelName"),
                                        doc.getString("manufacturer"),
                                        doc.getString("imgUrl"),
                                        Integer.parseInt(doc.get("price").toString()),
                                        Integer.parseInt(doc.get("inStock").toString()),
                                        Integer.parseInt(doc.get("standardMagCapacity").toString()),
                                        doc.getString("optionsMagCapacity"),
                                        doc.getString("caliber"),
                                        Integer.parseInt(doc.get("weight").toString()),
                                        Integer.parseInt(doc.get("barrelLength").toString()),
                                        Integer.parseInt(doc.get("triggerPull").toString())
                                );
                                gunArrryList.add(gun);
                            }
                            adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(addGun.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnAdd) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                    Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
                    EditText etPrice = dialogView.findViewById(R.id.etPrice);
                    EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                    EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
                    EditText etImageURL = dialogView.findViewById(R.id.etImageURL);
                    EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
                    EditText etStandardMagCapacity = dialogView.findViewById(R.id.etStandardMagCapacity);
                    EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                    EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                    EditText etWeight = dialogView.findViewById(R.id.etWeight);
                    EditText etBarrelSize = dialogView.findViewById(R.id.etBarrelSize);
                    EditText etTriggerPull = dialogView.findViewById(R.id.etTriggerPull);


                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String modelName = etToyName.getText().toString();
                            String stPrice = etPrice.getText().toString(); //remember conv to int
                            String manufacturer = etManufacturer.getText().toString();
                            String imgUrl = etImageURL.getText().toString();
                            String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
                            String stStandardMagCapacity = etStandardMagCapacity.getText().toString(); //remember conv to int
                            String magOptions = etMagOptions.getText().toString();
                            String caliber = etCaliber.getText().toString();
                            String stWeight = etWeight.getText().toString(); //remember conv to int
                            String stBarrelLength = etBarrelSize.getText().toString(); //remember conv to int
                            String stTriggerPull = etTriggerPull.getText().toString(); //remember conv to int


                            if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || imgUrl.isEmpty() || stInStock.isEmpty() || stStandardMagCapacity.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty() || stBarrelLength.isEmpty() || stTriggerPull.isEmpty()) {
                                Toast.makeText(addGun.this, "Please type both Name and Price", Toast.LENGTH_SHORT).show();
                            } else {
                                int price = Integer.parseInt(stPrice);
                                int inStock = Integer.parseInt(stInStock);
                                int standardMagCapacity = Integer.parseInt(stStandardMagCapacity);
                                int weight = Integer.parseInt(stWeight);
                                int barrelLength = Integer.parseInt(stBarrelLength);
                                int triggerPull = Integer.parseInt(stTriggerPull);
                                Gun gun = new Gun(modelName, manufacturer, imgUrl, price, inStock, standardMagCapacity, magOptions, caliber, weight, barrelLength, triggerPull);
                                firestore
                                        .collection("guns")
                                        .document(System.currentTimeMillis() + "")
                                        .set(gun)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(addGun.this, "Gun added!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(addGun.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    });

                    builder.setView(dialogView);
                    builder.create().show();
                }
            }
        });
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        List<DocumentSnapshot> docList = value.getDocuments();
        gunArrryList.clear();
        for (DocumentSnapshot doc : docList) {
            Gun gun = new Gun(
                    doc.getString("modelName"),
                    doc.getString("manufacturer"),
                    doc.getString("imgUrl"),
                    Integer.parseInt(doc.get("price").toString()),
                    Integer.parseInt(doc.get("inStock").toString()),
                    Integer.parseInt(doc.get("standardMagCapacity").toString()),
                    doc.getString("optionsMagCapacity"),
                    doc.getString("caliber"),
                    Integer.parseInt(doc.get("weight").toString()),
                    Integer.parseInt(doc.get("barrelLength").toString()),
                    Integer.parseInt(doc.get("triggerPull").toString())
            );
            gunArrryList.add(gun);
        }
        adapter.notifyDataSetChanged();
    }
}
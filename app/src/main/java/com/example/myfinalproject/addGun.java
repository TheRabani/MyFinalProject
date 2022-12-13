package com.example.myfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class addGun extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gun);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == btnAdd) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                    Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
                    EditText etPrice = dialogView.findViewById(R.id.etPrice);
                    EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                    EditText  etManufacturer= dialogView.findViewById(R.id.etManufacturer);
                    EditText  etImageURL= dialogView.findViewById(R.id.etImageURL);
                    EditText  etUnitsInStock= dialogView.findViewById(R.id.etInStock);
                    EditText  etStandardMagCapacity= dialogView.findViewById(R.id.etStandardMagCapacity);
                    EditText  etMagOptions= dialogView.findViewById(R.id.etMagOptions);
                    EditText  etCaliber= dialogView.findViewById(R.id.etCaliber);
                    EditText  etWeight= dialogView.findViewById(R.id.etWeight);
                    EditText  etBarrelSize= dialogView.findViewById(R.id.etBarrelSize);
                    EditText  etTriggerPull= dialogView.findViewById(R.id.etTriggerPull);


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


                            if(modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || imgUrl.isEmpty() || stInStock.isEmpty() || stStandardMagCapacity.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty() || stBarrelLength.isEmpty() || stTriggerPull.isEmpty())
                            {
                                Toast.makeText(addGun.this, "Please type both Name and Price", Toast.LENGTH_SHORT).show();
                            }
                            else{
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
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(addGun.this, "Gun added!", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
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

}
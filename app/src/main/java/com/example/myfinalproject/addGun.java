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
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);
                    EditText  = dialogView.findViewById(R.id.);


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
                            String caliber;
                            String stWeight; //remember conv to int
                            String stBarrelLength; //remember conv to int
                            String stTriggerPull; //remember conv to int


                            if(name.isEmpty() || price == null)
                            {
                                Toast.makeText(addGun.this, "Please type both Name and Price", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Gun toy = new Gun(name, price);
                                firestore
                                        .collection("toys")
                                        .document(System.currentTimeMillis() + "")
                                        .set(toy)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(addGun.this, "Toy added!", Toast.LENGTH_SHORT).show();
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
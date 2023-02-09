package com.example.myfinalproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addGun extends AppCompatActivity implements EventListener<QuerySnapshot> {
    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    public DocumentSnapshot doc2;
    public String st;

    boolean isOn;

    String tempManufacturer, tempModelName;

    ImageView image;
    Uri imageUri;
    StorageReference storageReference;
    public ProgressBar progressBar;

    private ListView gunListView;
    private gunAdapter adapter;

    public int num;

    private ArrayList<Gun> gunArrryList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gun);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = findViewById(R.id.btnAdd);

        Gun lastSelected;

        progressBar = findViewById(R.id.progressBar);

        gunArrryList = new ArrayList<Gun>();
        gunListView = findViewById(R.id.listViewGun);
        adapter = new gunAdapter(this, R.layout.gun_row, gunArrryList);


        gunListView.setAdapter(adapter);

        gunListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (!isOn) {
                Gun g = adapter.getItem(i);

//                progressBar.setVisibility(View.VISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
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
                Button updateBtn = dialogView.findViewById(R.id.update2);
                Button buttonEditUnits = dialogView.findViewById(R.id.editUnitsInStock);
//                Button editImage = dialogView.findViewById(R.id.editImage);
//                editImage.setVisibility(View.VISIBLE);
                buttonEditUnits.setVisibility(View.VISIBLE);
                updateBtn.setVisibility(View.VISIBLE);

                unitsInStock.setText("" + g.getInStock());
                magOptions.setText("" + g.getOptionsMagCapacity());
                caliber.setText("" + g.getCaliber());
                weight.setText("" + g.getWeight());
                price.setText("" + g.getPrice());


                makeAndModel.setText("" + g.getManufacturer() + " " + g.getModelName());
                storageReference = FirebaseStorage.getInstance().getReference().child("image/" + makeAndModel.getText().toString());

                    //                editImage.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
//                        View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_gun, null, false);
//                        builder.setView(dialogView);
//                        AlertDialog ad = builder.create();
//                        TextView textView = dialogView.findViewById(R.id.textView);
//                        textView.setText("Are You Sure You Want To Edit This Picture?");
//                        ad.show();
//                        ad.setCancelable(false);
//                        Button buttonYes = dialogView.findViewById(R.id.buttonYes);
//                        Button buttonNo = dialogView.findViewById(R.id.buttonNo);
//                        Button buttonConfirm = dialogView.findViewById(R.id.confirmBtn);
//
//                        buttonNo.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                isOn = false;
//                                ad.dismiss();
//                            }
//                        });
//
//                        buttonYes.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                storageReference.delete()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                isOn = false;
//
//                                                buttonConfirm.setVisibility(View.VISIBLE);
//
//                                                Intent intent = new Intent();
//                                                intent.setType("image/");
//                                                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                                                tempManufacturer = g.getManufacturer();
//                                                tempModelName = g.getModelName();
//
//                                                ActivityResultLauncher<Intent> launcher = registerForActivityResult(
//                                                        new ActivityResultContracts.StartActivityForResult(),
//                                                        new ActivityResultCallback<ActivityResult>() {
//                                                            @Override
//                                                            public void onActivityResult(ActivityResult result) {
//                                                                Intent data = result.getData();
//
//                                                            }
//                                                        }
//                                                );
//
//                                                startActivityForResult(intent, 100);
//
//                                                buttonConfirm.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View view) {
//
//                                                        storageReference = FirebaseStorage.getInstance().getReference("image/" + tempManufacturer + " " + tempModelName);
//                                                        storageReference.putFile(imageUri)
//                                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                                                    @Override
//                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                                        image.setImageURI(null);
//                                                                        Toast.makeText(addGun.this, "Image added", Toast.LENGTH_SHORT).show();
//                                                                        if (progressBar.getVisibility() == View.VISIBLE) {
//                                                                            progressBar.setVisibility(View.GONE);
//                                                                        }
//                                                                    }
//                                                                }).addOnFailureListener(new OnFailureListener() {
//                                                                    @Override
//                                                                    public void onFailure(@NonNull Exception e) {
//                                                                        if (progressBar.getVisibility() == View.VISIBLE) {
//                                                                            progressBar.setVisibility(View.GONE);
//                                                                            Toast.makeText(addGun.this, "Failed image upload", Toast.LENGTH_SHORT).show();
//                                                                        }
//
//                                                                    }
//                                                                });
//                                                    }
//                                                });
//
//
////                                                ad.dismiss();
//                                            }
//                                        });
//                            }
//                        });
//                    }
//                });


                imageView.setImageBitmap(ShopFragment.getBitmapFromName("" + g.getManufacturer() + " " + g.getModelName(), ShopFragment.nodeGunBitMap));
                ad.show();

//                try {
//
//                    File localFile = File.createTempFile("" + makeAndModel.getText().toString(), "jpeg");
//
//                    storageReference.getFile(localFile)
//                            .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
//                                    if (task.isSuccessful()) {
//                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                                        imageView.setImageBitmap(bitmap);
//
//                                        ad.show();
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                    } else {
//                                        Toast.makeText(addGun.this, "Error", Toast.LENGTH_SHORT).show();
//                                        imageView.setImageResource(R.drawable.x);
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                        ad.show();
//                                    }
//                                }
//                            });
//                } catch (IOException e) {
//                    Toast.makeText(addGun.this, "Error2", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.INVISIBLE);
//                    e.printStackTrace();
//                }


//                Picasso.get()
//                        .load("" + g.getImgUrl())
//                        .into(imageView);

//                ad.show();

                buttonEditUnits.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                        View dialogView = getLayoutInflater().inflate(R.layout.dialolg_add_units, null, false);
                        builder.setView(dialogView);
                        AlertDialog ad3 = builder.create();
                        EditText number = dialogView.findViewById(R.id.number);
                        FloatingActionButton addOne = dialogView.findViewById(R.id.floatingActionButtonAddOne);
                        FloatingActionButton subOne = dialogView.findViewById(R.id.floatingActionButtonSubOne);
                        Button apply = dialogView.findViewById(R.id.accept);
                        Button cancel = dialogView.findViewById(R.id.cancel);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ad3.dismiss();
                            }
                        });

                        apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                num = Integer.parseInt(number.getText().toString());
                                firestore
                                        .collection("guns")
                                        .document("" + g.getManufacturer() + " " + g.getName())
                                        .set(new Gun(g.getName(), g.getManufacturer(), /*g.getImgUrl(),*/ g.getPrice(), num, g.getOptionsMagCapacity(), g.getCaliber(), g.getWeight()))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    ad3.dismiss();
                                                    ad.dismiss();
                                                    Toast.makeText(addGun.this, "Updated!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(addGun.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });

                        number.setText("" + g.getInStock());
                        addOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                number.setText("" + (Integer.parseInt(number.getText().toString()) + 1));
                            }
                        });
                        subOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int num = Integer.parseInt(number.getText().toString());
                                if (num != 0)
                                    number.setText("" + (num - 1));
                            }
                        });

                        ad3.show();
                        ad3.setCancelable(false);

                    }
                });

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                        builder.setView(dialogView);
                        AlertDialog ad2 = builder.create();
                        Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);


                        EditText etPrice = dialogView.findViewById(R.id.etPrice);
                        EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                        EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
                        Button etImageURL = dialogView.findViewById(R.id.etImageURL);
                        etImageURL.setVisibility(View.GONE);
                        EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
//                    EditText etStandardMagCapacity = dialogView.findViewById(R.id.etStandardMagCapacity);
                        EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                        EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                        EditText etWeight = dialogView.findViewById(R.id.etWeight);
//                    EditText etBarrelSize = dialogView.findViewById(R.id.etBarrelSize);
//                    EditText etTriggerPull = dialogView.findViewById(R.id.etTriggerPull);
                        buttonAdd.setText("update");

                        etPrice.setText("" + g.getPrice());
                        etToyName.setText("" + g.getModelName());
                        etManufacturer.setText("" + g.getManufacturer());
//                        etImageURL.setText("" + g.getImgUrl());
                        etUnitsInStock.setText("" + g.getInStock());
                        etMagOptions.setText("" + g.getOptionsMagCapacity());
                        etCaliber.setText("" + g.getCaliber());
                        etWeight.setText("" + g.getWeight());


                        etManufacturer.setInputType(InputType.TYPE_NULL);
                        etManufacturer.setTextColor(Color.GRAY);
                        etManufacturer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        etToyName.setInputType(InputType.TYPE_NULL);
                        etToyName.setTextColor(Color.GRAY);
                        etToyName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                        ad2.show();

                        buttonAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ad2.dismiss();
                                String modelName = etToyName.getText().toString();
                                String stPrice = etPrice.getText().toString(); //remember conv to int
                                String manufacturer = etManufacturer.getText().toString();
                                String imgUrl = etImageURL.getText().toString();
                                String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
//                            String stStandardMagCapacity = etStandardMagCapacity.getText().toString(); //remember conv to int
                                String magOptions = etMagOptions.getText().toString();
                                String caliber = etCaliber.getText().toString();
                                String stWeight = etWeight.getText().toString(); //remember conv to int
//                            String stBarrelLength = etBarrelSize.getText().toString(); //remember conv to int
//                            String stTriggerPull = etTriggerPull.getText().toString(); //remember conv to int


                                if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || stInStock.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty()) {
                                    Toast.makeText(addGun.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                                } else {
                                    int price = Integer.parseInt(stPrice);
                                    int inStock = Integer.parseInt(stInStock);
//                                int standardMagCapacity = Integer.parseInt(stStandardMagCapacity);
                                    int weight = Integer.parseInt(stWeight);
//                                int barrelLength = Integer.parseInt(stBarrelLength);
//                                int triggerPull = Integer.parseInt(stTriggerPull);
                                    Gun gun = new Gun(modelName, manufacturer, /*imgUrl,*/ price, inStock, magOptions, caliber, weight);
                                    firestore
                                            .collection("guns")
                                            .document("" + manufacturer + " " + modelName)
                                            .set(gun)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(addGun.this, "Gun updated!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(addGun.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });


//                        builder.setView(dialogView);
//                        builder.create().show();

                    }
                });
            }
        });

//        gunListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Gun g= adapter.getItem(i);
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
//                View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_gun_details, null, false);
//                builder.setView(dialogView);
//                AlertDialog ad = builder.create();
//
//                ImageView imageView = dialogView.findViewById(R.id.imageGun);
//                TextView makeAndModel = dialogView.findViewById(R.id.makeAndModel);
//                TextView unitsInStock = dialogView.findViewById(R.id.unitsInStock);
//                TextView magOptions = dialogView.findViewById(R.id.magOptions);
//                TextView caliber = dialogView.findViewById(R.id.caliber);
//                TextView weight = dialogView.findViewById(R.id.weight);
//                TextView price = dialogView.findViewById(R.id.price);
//
//                makeAndModel.setText(""+g.getManufacturer()+" "+g.getModelName());
//                unitsInStock.setText(""+g.getInStock());
//                magOptions.setText(""+g.getOptionsMagCapacity());
//                caliber.setText(""+g.getCaliber());
//                weight.setText(""+g.getWeight());
//                price.setText(""+g.getPrice());
//                Picasso.get()
//                        .load(""+g.getImgUrl())
//                        .into(imageView);
//                ad.show();
//            }
//        });

        gunListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isOn = true;

                Gun g = adapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_gun, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();
                ad.show();
                ad.setCancelable(false);
                Button buttonYes = dialogView.findViewById(R.id.buttonYes);
                Button buttonNo = dialogView.findViewById(R.id.buttonNo);

                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        DatabaseReference current = FirebaseDatabase.getInstance().getReference("guns").child(""+g.getManufacturer()+" "+g.getModelName());

                        firestore.collection("guns")
                                .document("" + g.getManufacturer() + " " + g.getModelName())
                                .delete();

                        startActivity(new Intent(addGun.this, MainActivity.class));
                        startActivity(new Intent(getIntent()));

                        Toast.makeText(addGun.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        isOn = false;
                        ad.dismiss();

//                        finish();
//                        startActivity(getIntent());


                    }
                });

                buttonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isOn = false;
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
//        firestore.collection("guns")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            List<DocumentSnapshot> docList = task.getResult().getDocuments();
//                            gunArrryList.clear();
//                            for (DocumentSnapshot doc : docList) {
//                                Gun gun = new Gun(
//                                        doc.getString("modelName"),
//                                        doc.getString("manufacturer"),
////                                            doc.getString("imgUrl"),
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
//                            }
//                            adapter.notifyDataSetChanged();
//                        } else
//                            Toast.makeText(addGun.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnAdd) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(addGun.this);
                    View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                    builder.setView(dialogView);
                    AlertDialog ad4 = builder.create();
                    Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
                    EditText etPrice = dialogView.findViewById(R.id.etPrice);
                    EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                    EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);

                    Button etImageURL = dialogView.findViewById(R.id.etImageURL);

                    EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
//                    EditText etStandardMagCapacity = dialogView.findViewById(R.id.etStandardMagCapacity);
                    EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                    EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                    EditText etWeight = dialogView.findViewById(R.id.etWeight);
//                    EditText etBarrelSize = dialogView.findViewById(R.id.etBarrelSize);
//                    EditText etTriggerPull = dialogView.findViewById(R.id.etTriggerPull);

//                    etImageURL.setPaintFlags(etImageURL.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    image = dialogView.findViewById(R.id.firebaseImage);

                    etImageURL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, 100);
//                            image.setImageURI(imageUri);
                        }
                    });


                    buttonAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String modelName = etToyName.getText().toString();
                            String stPrice = etPrice.getText().toString(); //remember conv to int
                            String manufacturer = etManufacturer.getText().toString();


//                            String imgUrl = etImageURL.getText().toString(); ------------------------------------------

                            String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
//                            String stStandardMagCapacity = etStandardMagCapacity.getText().toString(); //remember conv to int
                            String magOptions = etMagOptions.getText().toString();
                            String caliber = etCaliber.getText().toString();
                            String stWeight = etWeight.getText().toString(); //remember conv to int
//                            String stBarrelLength = etBarrelSize.getText().toString(); //remember conv to int
//                            String stTriggerPull = etTriggerPull.getText().toString(); //remember conv to int

                            if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || /*imgUrl.isEmpty() ||*/ stInStock.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty() || imageUri == null) {
                                Toast.makeText(addGun.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            } else {
                                progressBar.setVisibility(View.VISIBLE);

                                int price = Integer.parseInt(stPrice);
                                int inStock = Integer.parseInt(stInStock);
//                                int standardMagCapacity = Integer.parseInt(stStandardMagCapacity);
                                int weight = Integer.parseInt(stWeight);
//                                int barrelLength = Integer.parseInt(stBarrelLength);
//                                int triggerPull = Integer.parseInt(stTriggerPull);
                                Gun gun = new Gun(modelName, manufacturer, /*imgUrl,*/ price, inStock, magOptions, caliber, weight);
                                firestore
                                        .collection("guns")
                                        .document("" + manufacturer + " " + modelName)
                                        .set(gun)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(addGun.this, "Gun added!", Toast.LENGTH_SHORT).show();

                                                    storageReference = FirebaseStorage.getInstance().getReference("image/" + manufacturer + " " + modelName);
                                                    storageReference.putFile(imageUri)
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    image.setImageURI(null);
                                                                    Toast.makeText(addGun.this, "Image added", Toast.LENGTH_SHORT).show();

                                                                    recreate();

                                                                    if (progressBar.getVisibility() == View.VISIBLE) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    if (progressBar.getVisibility() == View.VISIBLE) {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(addGun.this, "Failed image upload", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(addGun.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                                int a = 0;
//                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                                String id = currentUser.getUid();
//                                Request request = new Request(id);
//                                firestore.
//                                        collection("requests")
//                                        .document("" + manufacturer + " " + modelName)
//                                        .set(request)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(!task.isSuccessful())
//                                                    Toast.makeText(addGun.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
                                ad4.dismiss();
                            }
//                            ad4.dismiss();
                        }
                    });


                    ad4.show();
                }
            }
        });
    }

    private void addPic(String s) {
        storageReference = FirebaseStorage.getInstance().getReference("image/" + s);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        image.setImageURI(null);
                        Toast.makeText(addGun.this, "Image added", Toast.LENGTH_SHORT).show();
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(addGun.this, "Failed image upload", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        List<DocumentSnapshot> docList = value.getDocuments();
        gunArrryList.clear();
        if (docList.toArray().length != 0)
            for (DocumentSnapshot doc : docList) {
                Gun gun = new Gun(
                        doc.getString("modelName"),
                        doc.getString("manufacturer"),
//                             doc.getString("imgUrl"),
                        Integer.parseInt(doc.get("price").toString()),
                        Integer.parseInt(doc.get("inStock").toString()),
                        //                    Integer.parseInt(doc.get("standardMagCapacity").toString()),
                        doc.getString("optionsMagCapacity"),
                        doc.getString("caliber"),
                        Integer.parseInt(doc.get("weight").toString())
                        //                    Integer.parseInt(doc.get("barrelLength").toString()),
                        //                    Integer.parseInt(doc.get("triggerPull").toString())
                );
                gunArrryList.add(gun);
            }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();


            try {
                image.setImageURI(imageUri);
            } catch (Exception e) {
                Toast.makeText(this, "faillll", Toast.LENGTH_SHORT).show();
            }


        }
    }
}
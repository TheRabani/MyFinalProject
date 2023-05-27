package com.example.myfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class admin_fragment_add_gun extends Fragment implements EventListener<QuerySnapshot>, SelectListener {

    private FirebaseFirestore firestore;

    ProgressBar adminProgressBar;

    RecyclerView normal_rec;

    int countBit = 0;

    AlertDialog tempAd;

    //    public static GunBitMap[] gunBitMap;
    public static Node<GunBitMap> AdminNodeGunBitMap;

    public int count = 0;
    Bitmap[] bitmap;
    int howMany = 0;

    private /*ListView*/ RecyclerView gunListView;
    private static gunAdapter adapter;

    ImageView tvImage;

    private ArrayList<Gun> gunArrayList;
    Uri imageUri;
    ImageView image;
    StorageReference storageReference;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_admin_add_gun, container, false);
        firestore = FirebaseFirestore.getInstance();
        tvImage = view.findViewById(R.id.gunImage);
        adminProgressBar = view.findViewById(R.id.adminProgressBar);

        FloatingActionButton addGun = view.findViewById(R.id.adminBtnAdd);
        addGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_gun, null, false);
                builder.setView(dialogView);
                AlertDialog ad4 = builder.create();
                Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
                EditText etPrice = dialogView.findViewById(R.id.etPrice);
                EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
                Button etImageURL = dialogView.findViewById(R.id.etImageURL);
                EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
                EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                EditText etWeight = dialogView.findViewById(R.id.etWeight);
                image = dialogView.findViewById(R.id.firebaseImage);

                etImageURL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 100);
                        image.setImageURI(imageUri);
                    }
                });


                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String modelName = etToyName.getText().toString();
                        String stPrice = etPrice.getText().toString(); //remember conv to int
                        String manufacturer = etManufacturer.getText().toString();
                        String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
                        String magOptions = etMagOptions.getText().toString();
                        String caliber = etCaliber.getText().toString();
                        String stWeight = etWeight.getText().toString(); //remember conv to int

                        etImageURL.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setType("image/");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 100);
                                image.setImageURI(imageUri);
                            }
                        });

                        if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || /*imgUrl.isEmpty() ||*/ stInStock.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty() || imageUri == null) {
                            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            if (modelName.isEmpty())
                                shake(dialogView, R.id.etGunModel);
                            if (stPrice.isEmpty())
                                shake(dialogView, R.id.etPrice);
                            if (manufacturer.isEmpty())
                                shake(dialogView, R.id.etManufacturer);
                            if (stInStock.isEmpty())
                                shake(dialogView, R.id.etInStock);
                            if (magOptions.isEmpty())
                                shake(dialogView, R.id.etMagOptions);
                            if (caliber.isEmpty())
                                shake(dialogView, R.id.etCaliber);
                            if (stWeight.isEmpty())
                                shake(dialogView, R.id.etWeight);
                            if (imageUri == null)
                                shake(dialogView, R.id.etImageURL);

                        } else {
                            adminProgressBar.setVisibility(View.VISIBLE);

                            int price = Integer.parseInt(stPrice);
                            int inStock = Integer.parseInt(stInStock);
                            int weight = Integer.parseInt(stWeight);
                            Gun gun = new Gun(modelName, manufacturer, /*imgUrl,*/ price, inStock, magOptions, caliber, weight);
                            firestore
                                    .collection("guns")
                                    .document("" + manufacturer + " " + modelName)
                                    .set(gun)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Gun added!", Toast.LENGTH_SHORT).show();

                                                storageReference = FirebaseStorage.getInstance().getReference("image/" + manufacturer + " " + modelName);
                                                storageReference.putFile(imageUri)
                                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                image.setImageURI(null);
                                                                Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
                                                                if (adminProgressBar.getVisibility() == View.VISIBLE) {
                                                                    adminProgressBar.setVisibility(View.GONE);
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                if (adminProgressBar.getVisibility() == View.VISIBLE) {
                                                                    adminProgressBar.setVisibility(View.GONE);
                                                                    Toast.makeText(getContext(), "Failed image upload", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            int a = 0;
                            ad4.dismiss();
                        }
                    }
                });

                ad4.show();
            }
        });

        gunArrayList = new ArrayList<Gun>();
        gunListView = view.findViewById(R.id./*listViewGun*/recyclerView);
        if (adapter == null)
            adapter = new gunAdapter(getActivity()/*, R.layout.gun_row*/, gunArrayList, this);
        gunListView.setAdapter(adapter);

//        -------------------------------------------------------------------------------------------------------------------
        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(getActivity());
        View tempDialogView = getLayoutInflater().inflate(R.layout.dialog_loading, null, false);
        tempBuilder.setView(tempDialogView);
        tempAd = tempBuilder.create();
        tempAd.setCancelable(false);

        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                tempAd.cancel();
            }
        };
        mHandler.postDelayed(mRunnable, 15 * 1000);//Execute after 15 Seconds

        if (AdminNodeGunBitMap == null) {
            tempAd.show();
            firestore
                    .collection("guns")
                    .addSnapshotListener(admin_fragment_add_gun.this);
        } else {
            count = 0;
            firestore.collection("guns")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<DocumentSnapshot> docList = task.getResult().getDocuments();
                                gunArrayList.clear();
                                for (DocumentSnapshot doc : docList) {
                                    count++;
                                    Gun gun = new Gun(
                                            doc.getString("modelName"),
                                            doc.getString("manufacturer"),
                                            Integer.parseInt(doc.get("price").toString()),
                                            Integer.parseInt(doc.get("inStock").toString()),
                                            doc.getString("optionsMagCapacity"),
                                            doc.getString("caliber"),
                                            Integer.parseInt(doc.get("weight").toString())
                                    );
                                    gunArrayList.add(gun);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

        return view;

    }

    private void shake(View dialogView, int id) {
        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(0)
                .playOn(dialogView.findViewById(id));
    }

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        List<DocumentSnapshot> docList = value.getDocuments();
        gunArrayList.clear();
        count = 0;
        if (docList.size() != 0) {
            for (DocumentSnapshot doc : docList) {
                count++;
                Gun gun = new Gun(
                        doc.getString("modelName"),
                        doc.getString("manufacturer"),
                        Integer.parseInt(doc.get("price").toString()),
                        Integer.parseInt(doc.get("inStock").toString()),
                        doc.getString("optionsMagCapacity"),
                        doc.getString("caliber"),
                        Integer.parseInt(doc.get("weight").toString())
                );
                gunArrayList.add(gun);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/" + doc.getString("manufacturer") + " " + doc.getString("modelName"));
                try {
                    File localFile = File.createTempFile(doc.getString("manufacturer") + " " + doc.getString("modelName"), "jpeg");

                    storageReference.getFile(localFile)
                            .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Bitmap tempBitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        if (AdminNodeGunBitMap == null)
                                            AdminNodeGunBitMap = new Node<GunBitMap>(new GunBitMap(doc.getString("manufacturer") + " " + doc.getString("modelName"), tempBitmap));
                                        else {
                                            Node<GunBitMap> temp = getLastNode(AdminNodeGunBitMap);
                                            temp.setNext(new Node<GunBitMap>(new GunBitMap(doc.getString("manufacturer") + " " + doc.getString("modelName"), tempBitmap)));
                                        }
                                    } else {
                                    }
                                    if (count == docList.size()) {
                                        adapter.notifyDataSetChanged();

                                        Runnable mRunnable2;
                                        Handler mHandler2 = new Handler();
                                        mRunnable2 = new Runnable() {
                                            @Override
                                            public void run() {
                                                tempAd.dismiss();
                                            }
                                        };
                                        mHandler2.postDelayed(mRunnable2, 4 * 1000);//Execute after 10 Seconds
                                    }
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
//            tempAd.dismiss();
            adapter.notifyDataSetChanged();
        }
    }

    public static Bitmap getBitmapFromName(String name, Node<GunBitMap> node) {
        Node<GunBitMap> g = node;
        while (g != null && g.getValue() != null && !g.getValue().getName().equals(name))
            g = g.getNext();

        if (g == null || g.getValue() == null) {
            return null;
        }
        return g.getValue().getBitmap();
    }

    public static Bitmap getBitmapFromName(String name) {
        Node<GunBitMap> g = AdminNodeGunBitMap;
        while (g != null && g.getValue() != null && !g.getValue().getName().equals(name))
            g = g.getNext();

        if (g == null || g.getValue() == null) {
            return null;
        }
        return g.getValue().getBitmap();
    }

    public static Node<GunBitMap> getLastNode(Node<GunBitMap> node) {
        Node<GunBitMap> n = node;
        while (n.getNext() != null)
            n = n.getNext();
        return n;
    }

    @Override
    public void onItemClicked(Schedule schedule) {

    }

    @Override
    public void onItemClicked(String string, String time) {

    }

    @Override
    public void onItemClicked(Gun g) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
        String mnm = "" + g.getManufacturer() + " " + g.getModelName();
        makeAndModel.setText(mnm);
        imageView.setImageBitmap(getBitmapFromName(mnm));

        ad.show();

        buttonEditUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        int num = Integer.parseInt(number.getText().toString());
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
                                            Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_gun, null, false);
                builder.setView(dialogView);
                AlertDialog ad2 = builder.create();
                Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);


                EditText etPrice = dialogView.findViewById(R.id.etPrice);
                EditText etToyName = dialogView.findViewById(R.id.etGunModel);
                EditText etManufacturer = dialogView.findViewById(R.id.etManufacturer);
                EditText etUnitsInStock = dialogView.findViewById(R.id.etInStock);
                EditText etMagOptions = dialogView.findViewById(R.id.etMagOptions);
                EditText etCaliber = dialogView.findViewById(R.id.etCaliber);
                EditText etWeight = dialogView.findViewById(R.id.etWeight);

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
                        String modelName = etToyName.getText().toString();
                        String stPrice = etPrice.getText().toString(); //remember conv to int
                        String manufacturer = etManufacturer.getText().toString();
                        String stInStock = etUnitsInStock.getText().toString(); //remember conv to int
//                            String stStandardMagCapacity = etStandardMagCapacity.getText().toString(); //remember conv to int
                        String magOptions = etMagOptions.getText().toString();
                        String caliber = etCaliber.getText().toString();
                        String stWeight = etWeight.getText().toString(); //remember conv to int
//                            String stBarrelLength = etBarrelSize.getText().toString(); //remember conv to int
//                            String stTriggerPull = etTriggerPull.getText().toString(); //remember conv to int


                        if (modelName.isEmpty() || stPrice.isEmpty() || manufacturer.isEmpty() || stInStock.isEmpty() || magOptions.isEmpty() || caliber.isEmpty() || stWeight.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                            if (modelName.isEmpty())
                                shake(dialogView, R.id.etGunModel);
                            if (stPrice.isEmpty())
                                shake(dialogView, R.id.etPrice);
                            if (manufacturer.isEmpty())
                                shake(dialogView, R.id.etManufacturer);
                            if (stInStock.isEmpty())
                                shake(dialogView, R.id.etInStock);
                            if (magOptions.isEmpty())
                                shake(dialogView, R.id.etMagOptions);
                            if (caliber.isEmpty())
                                shake(dialogView, R.id.etCaliber);
                            if (stWeight.isEmpty())
                                shake(dialogView, R.id.etWeight);
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
                                            ad2.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Gun updated!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                image.setImageURI(imageUri);
            } catch (Exception e) {
                Toast.makeText(getContext(), "faillll", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemLongClicked(Gun g) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_save_spot, null, false);
        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        ad.setCancelable(false);
        Button btnYes = dialogView.findViewById(R.id.buttonYes);
        Button btnNo = dialogView.findViewById(R.id.buttonNo);
        TextView textView = dialogView.findViewById(R.id.textView);
        textView.setText("למחוק את " + g.getManufacturer() + " " + g.getModelName() + " " + "מרשימת האקדחים" + "?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore
                        .collection("guns")
                        .document("" + g.getManufacturer() + " " + g.getModelName())
                        .delete();
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image/" + g.getManufacturer() + " " + g.getModelName());
                storageReference.delete();
                adapter.notifyDataSetChanged();
                ad.dismiss();
            }
        });
        btnNo.setOnClickListener(view -> ad.dismiss());
        ad.show();
    }

}
package com.example.myfinalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompatSideChannelService;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.meta.When;

public class ShopFragment extends Fragment implements EventListener<QuerySnapshot>, SelectListener {

    private FirebaseFirestore firestore;

    RecyclerView normal_rec;

    int countBit = 0;

    AlertDialog tempAd;

    //    public static GunBitMap[] gunBitMap;
    public static Node<GunBitMap> nodeGunBitMap;

    public int count = 0;
    Bitmap[] bitmap;
    int howMany = 0;

    private /*ListView*/RecyclerView gunListView;
    private static gunAdapter adapter;

    ImageView tvImage;

    private ArrayList<Gun> gunArrayList;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);
        firestore = FirebaseFirestore.getInstance();
        tvImage = view.findViewById(R.id.gunImage);

        gunArrayList = new ArrayList<Gun>();
        gunListView = view.findViewById(R.id./*listViewGun*/recyclerView);
        if (adapter == null)
            adapter = new gunAdapter(getActivity()/*, R.layout.gun_row*/, gunArrayList, this);
        gunListView.setAdapter(adapter);

//        normal_rec = view.findViewById(R.id.recyclerView);
//        normal_rec.setLayoutManager(new LinearLayoutManager(getContext()));               fgewquhvfbjhwbvfjhnwdsvhjgsdfjhfv


//        -------------------------------------------------------------------------------------------------------------------
        AlertDialog.Builder tempBuilder = new AlertDialog.Builder(getActivity());
        View tempDialogView = getLayoutInflater().inflate(R.layout.dialog_loading, null, false);
        tempBuilder.setView(tempDialogView);
        tempAd = tempBuilder.create();
        tempAd.setCancelable(false);
//        tempAd.show();


        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                tempAd.cancel();
            }
        };
        mHandler.postDelayed(mRunnable, 15 * 1000);//Execute after 15 Seconds




        if (nodeGunBitMap == null) {
            tempAd.show();
            firestore
                    .collection("guns")
                    .addSnapshotListener(ShopFragment.this);
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
                                        if (nodeGunBitMap == null)
                                            nodeGunBitMap = new Node<GunBitMap>(new GunBitMap(doc.getString("manufacturer") + " " + doc.getString("modelName"), tempBitmap));
                                        else {
                                            Node<GunBitMap> temp = getLastNode(nodeGunBitMap);
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
                                        mHandler2.postDelayed(mRunnable2, 2 * 1000);//Execute after 10 Seconds
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

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
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
        Node<GunBitMap> g = nodeGunBitMap;
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
    public void onItemLongClicked(Schedule schedule) {

    }

    @Override
    public void onItemLongClicked(String string, String time) {

    }

    @Override
    public void onItemClicked(Gun gun) {
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

        makeAndModel.setText("" + gun.getManufacturer() + " " + gun.getModelName());

        Bitmap bit = getBitmapFromName("" + gun.getManufacturer() + " " + gun.getModelName(), nodeGunBitMap);
        if (bit != null)
            imageView.setImageBitmap(bit);
        else
            imageView.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.x));

        int num = gun.getInStock();
        if (num != 0)
            unitsInStock.setText("" + gun.getInStock());
        else {
            unitsInStock.setText("0");
            request.setVisibility(View.VISIBLE);
        }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        magOptions.setText("" + gun.getOptionsMagCapacity());
        caliber.setText("" + gun.getCaliber());
        weight.setText("" + gun.getWeight());
        price.setText("" + gun.getPrice());
//                Picasso.get()
//                        .load("" + g.getImgUrl())
//                        .into(imageView);
        ad.show();
    }

    @Override
    public void onItemLongClicked(MansInfo mansInfo) {

    }

    @Override
    public void onItemLongClicked(Gun gun) {
        Toast.makeText(getActivity(), "long", Toast.LENGTH_SHORT).show();
    }
}
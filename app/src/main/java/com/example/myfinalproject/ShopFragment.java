package com.example.myfinalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment implements EventListener<QuerySnapshot> {

    private FirebaseFirestore firestore;
    private FloatingActionButton btnAdd;

    public DocumentSnapshot doc2;
    public String st;

    private ListView gunListView;
    private gunAdapter adapter;

    private ArrayList<Gun> gunArrryList;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        firestore = FirebaseFirestore.getInstance();
        btnAdd = view.findViewById(R.id.btnAdd);

        Gun lastSelected;

        gunArrryList = new ArrayList<Gun>();
        gunListView = view.findViewById(R.id.listViewGun);
        adapter = new gunAdapter(getActivity(), R.layout.gun_row, gunArrryList);


        gunListView.setAdapter(adapter);

        gunListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gun g= adapter.getItem(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_show_gun_details, null, false);
                builder.setView(dialogView);
                AlertDialog ad = builder.create();

                TextView makeAndModel = ad.findViewById(R.id.makeAndModel);
                TextView unitsInStock = ad.findViewById(R.id.unitsInStock);
                TextView magOptions = ad.findViewById(R.id.magOptions);
                TextView caliber = ad.findViewById(R.id.caliber);
                TextView weight = ad.findViewById(R.id.weight);
                TextView price = ad.findViewById(R.id.price);

//                makeAndModel.setText(""+g.getManufacturer()+" "+g.getModelName());
//                unitsInStock.setText(""+g.getInStock());
//                magOptions.setText(""+g.getOptionsMagCapacity());
//                caliber.setText(""+g.getCaliber());
//                weight.setText(""+g.getWeight());
//                price.setText(""+g.getPrice());
                ad.show();
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
//                                        Integer.parseInt(doc.get("standardMagCapacity").toString()),
                                        doc.getString("optionsMagCapacity"),
                                        doc.getString("caliber"),
                                        Integer.parseInt(doc.get("weight").toString())
//                                        Integer.parseInt(doc.get("barrelLength").toString()),
//                                        Integer.parseInt(doc.get("triggerPull").toString())
                                );
                                gunArrryList.add(gun);
                            }
                            adapter.notifyDataSetChanged();
                        } else
                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        return view;
    }

//    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
        List<DocumentSnapshot> docList = value.getDocuments();
        gunArrryList.clear();
        if(docList.toArray().length !=0)
            for (DocumentSnapshot doc : docList) {
                Gun gun = new Gun(
                        doc.getString("modelName"),
                        doc.getString("manufacturer"),
                        doc.getString("imgUrl"),
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

    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }
}
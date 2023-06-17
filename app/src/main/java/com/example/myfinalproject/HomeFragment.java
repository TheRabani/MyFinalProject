package com.example.myfinalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.maps.SupportMapFragment;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements SelectListener, OnMapReadyCallback {
    FirebaseAuth mAuth;
    Button logout, addGun;
    Button buttonGoTo, buttonNavigation;
    View view;
    RecyclerView recyclerView;
    public static ArrayList<String> book_id = new ArrayList<>(), book_date = new ArrayList<>(), book_time = new ArrayList<>();
    MyDatabaseHelper myDB;
    CustomAdapterSQLite customAdapterSQLite;
    ImageView image;
    static Activity activity;
    public static Fragment fragment;
    public static SupportMapFragment mapFragment;
    SliderView sliderView;
    int[] images = {R.drawable.krav1, R.drawable.krav2,R.drawable.krav3, R.drawable.krav4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        image = view.findViewById(R.id.imageNoData);

        sliderView = view.findViewById(R.id.image_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.VERTICALFLIPTRANSFORMATION);
        sliderView.startAutoCycle();

        recyclerView = view.findViewById(R.id.recyclerView);
        myDB = new MyDatabaseHelper(getContext());
        if (book_date.size() == 0) {
            storeDataInArrays();
        } else
            image.setVisibility(View.INVISIBLE);
        customAdapterSQLite = new CustomAdapterSQLite(getContext(), book_id, book_date, book_time, this);
        recyclerView.setAdapter(customAdapterSQLite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        buttonNavigation = view.findViewById(R.id.buttonNavigation);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.activity_notification, null, false);
        builder.setView(dialogView);
        AlertDialog ad = builder.create();
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.frame_layout2);
        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mapFragment.getMapAsync(HomeFragment.this);
            }
        };
        mHandler.postDelayed(mRunnable, 1 * 1000);//Execute after 15 Seconds
        buttonNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ad.show();

                dialogView.findViewById(R.id.btnWaze).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            // Launch Waze to look for Hawaii:
                            String url = "https://waze.com/ul?q=מטווח קרב&navigate=yes";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            // If Waze is not installed, open it in Google Play:
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.waze"));
                            startActivity(intent);
                        }
                    }
                });

                dialogView.findViewById(R.id.btnGoogle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("geo:" + 31.752167930459038 + "," + 35.21662087865303));
                        Intent chooser = Intent.createChooser(intent, "Lunch Maps");
                        startActivity(chooser);

                    }
                });
            }
        });

        mAuth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), Login.class));
            }
        });


//        addGun = (Button) view.findViewById(R.id.addGun);

//        addGun.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), addGun.class));
//            }
//        });

        return view;
    }

    void storeDataInArrays() {
        myDB = new MyDatabaseHelper(getContext());
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0)
            image.setVisibility(View.VISIBLE);
        else {
            image.setVisibility(View.INVISIBLE);
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_time.add(cursor.getString(2));
            }
        }
    }

    @Override
    public void onItemLongClicked(Schedule schedule) {
    }

    @Override
    public void onItemLongClicked(String string, String time) {
//        Toast.makeText(getContext(), ""+string, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), ""+book_id, Toast.LENGTH_SHORT).show();
        int second = string.indexOf('M'), third = string.indexOf('Y');
        String date = string.substring(1, second) + "-" + string.substring(second + 1, third) + "-" + string.substring(third + 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete your appointment on " + date + " at " + time);
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (string != null && !string.equals("")) {
                    myDB.deleteOneRow(string, time);
                    dialogInterface.dismiss();
                    restoreDataInArrays();
                    deleteFromArrayLists(string);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) // בודק האם ניתן אישור לשליחה
            a(MyDatabaseHelper.realDate, MyDatabaseHelper.realTime);
        else
            Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show(); // מודיע שלא ניתן אישור
    }

    public static void a(String date, String time) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 100);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            int second = date.indexOf('M'), third = date.indexOf('Y');
            String real = date.substring(1, second) + "-" + date.substring(second + 1, third) + "-" + date.substring(third + 1);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "נרשמת בהצלחה לאימון בתאריך" + " " + real + " " + "בשעה" + " " + time, null, null);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }

    @Override
    public void onItemClicked(Gun gun) {

    }

    @Override
    public void onItemLongClicked(MansInfo mansInfo) {

    }

    @Override
    public void onItemLongClicked(Gun gun) {

    }

    public static void deleteFromArrayLists(String date) {
        book_id.remove(date);
        book_date.remove(date);
        book_time.remove(date);
    }

    void restoreDataInArrays() {
        myDB = new MyDatabaseHelper(getContext());
        Cursor cursor = myDB.readAllData();
        book_id.clear();
        book_date.clear();
        book_time.clear();
        if (cursor.getCount() == 0)
            image.setVisibility(View.VISIBLE);
        else {
            image.setVisibility(View.INVISIBLE);
            while (cursor.moveToNext()) {
                book_id.add(cursor.getString(0));
                book_date.add(cursor.getString(1));
                book_time.add(cursor.getString(2));
            }
        }
        customAdapterSQLite.notifyDataSetChanged();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng kravLatLng = new LatLng(31.752167930459038, 35.21662087865303);
        markerOptions.position(kravLatLng);
        markerOptions.title("מטווח קרב-" + " " + "תלפיות יד חרוצים 13");
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("Krav", 150, 100)));

        MarkerOptions markerOptions2 = new MarkerOptions();
        LatLng kravLatLng2 = new LatLng(MainActivity.addresses.get(0).getLatitude(), MainActivity.addresses.get(0).getLongitude());
        markerOptions2.position(kravLatLng2);
        markerOptions2.title("המיקום שלך");
        markerOptions2.visible(true);
        markerOptions2.draggable(false);
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


        googleMap.clear();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kravLatLng, 8));
        googleMap.addMarker(markerOptions);
//        marker.showInfoWindow();
        Marker marker1 = googleMap.addMarker(markerOptions2);
        marker1.showInfoWindow();

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //alert dialog with picture
                return false;
            }
        });
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.red_flag);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
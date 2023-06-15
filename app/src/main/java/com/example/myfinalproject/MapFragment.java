package com.example.myfinalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Locale;

public class MapFragment extends Fragment {
    SupportMapFragment supportMapFragment;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP);

        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
//                        Toast.makeText(getContext(), "started", Toast.LENGTH_SHORT).show();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng kravLatLng = new LatLng(31.752167930459038, 35.21662087865303);
                        markerOptions.position(kravLatLng);
                        markerOptions.title("מטווח קרב-"+" "+"תלפיות יד חרוצים 13");
                        markerOptions.draggable(false);

                        MarkerOptions markerOptions2 = new MarkerOptions();
                        LatLng kravLatLng2 = new LatLng(MainActivity.addresses.get(0).getLatitude(), MainActivity.addresses.get(0).getLongitude());
                        markerOptions2.position(kravLatLng2);
                        markerOptions2.title("המיקום שלך");
                        markerOptions2.draggable(false);

                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kravLatLng, 14));
                        googleMap.addMarker(markerOptions);
                        googleMap.addMarker(markerOptions2);
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(@NonNull Marker marker) {
                                //alert dialog with picture
                                return false;
                            }
                        });
                    }
                });
            }
        };
        mHandler.postDelayed(mRunnable, 5 * 1000);//Execute after 15 Seconds







        return view;
    }

}
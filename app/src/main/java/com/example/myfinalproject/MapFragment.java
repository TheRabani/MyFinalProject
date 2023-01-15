package com.example.myfinalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    AlertDialog ad;
    View dialogView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.MY_MAP);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng kravLatLng = new LatLng(31.752167930459038, 35.21662087865303);
                markerOptions.position(kravLatLng)
                        .title("מטווח קרב")
                        .snippet("תלפיות יד חרוצים 13")
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kravLatLng, 17));
                googleMap.addMarker(markerOptions).showInfoWindow();


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                dialogView = getLayoutInflater().inflate(R.layout.dialog_krav, null, false);
                builder.setView(dialogView);
                ad = builder.create();

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        //alert dialog with pictures


                        return false;
                    }
                });
            }
        });


        return view;
    }
}
package com.example.go4lunch.controller.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private double mLatitude;
    private double mLongitude;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_maps_map_view);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext())); // Location Services

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Dexter Runtime permission for location
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        googleMap.setMyLocationEnabled(true); // Enable MyLocation Button
                        clientLocation(googleMap); // Locate user and move the camera to his position
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void clientLocation(GoogleMap googleMap) {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // Logic to handle location object
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        moveCameraOnUser(googleMap); // Set camera position to user location
                    }
                });
    }

    private void moveCameraOnUser(GoogleMap googleMap) {
        LatLng userPosition = new LatLng(mLatitude, mLongitude); // Get User Latitude and longitude position
        googleMap.setMaxZoomPreference(18);
        googleMap.setMinZoomPreference(15);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition)); // Move the camera to user position
    }
}
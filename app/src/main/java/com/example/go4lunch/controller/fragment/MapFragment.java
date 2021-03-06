package com.example.go4lunch.controller.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.Constant;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.example.go4lunch.controller.activities.RestaurantDetailsActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback, DoSearch {
    private double mLatitude;
    private double mLongitude;

    public static LatLng mUserPosition;

    private FusedLocationProviderClient mFusedLocationClient;
    private PlacesClient mPlacesClient;

    GoogleMap mGoogleMap;

    List<Place> placeList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        // Initialize the SDK
        Places.initialize(Objects.requireNonNull(getContext()), getString(R.string.google_api_key));
        // Create a new Places client instance
        mPlacesClient = Places.createClient(Objects.requireNonNull(getContext()));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_maps_map_view);
        if (mapFragment != null)
            mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext())); // Location Services

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Objects.requireNonNull(getContext()), R.raw.map_style));
        // Dexter Runtime permission for location
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mGoogleMap.setMyLocationEnabled(true); // Enable MyLocation Button
                        clientLocation(); // Locate user and move the camera to his position
                        moveCameraOnUser();
                        initPlaces();
                        clickOnMarker();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }

    private void clientLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), location -> {
                    if (location != null) {
                        // Logic to handle location object
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();

                        moveCameraOnUser(); // Set camera position to user location
                    }
                });
    }

    private void moveCameraOnUser() {
        mUserPosition = new LatLng(mLatitude, mLongitude); // Get User Latitude and longitude position
        mGoogleMap.setMaxZoomPreference(20);
        mGoogleMap.setMinZoomPreference(16);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mUserPosition)); // Move the camera to user position
    }

    private void initPlaces() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.LAT_LNG,
                Place.Field.TYPES,
                Place.Field.ID,
                Place.Field.NAME);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindCurrentPlaceResponse response = task.getResult();
                assert response != null;

                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                    if (Objects.requireNonNull(placeLikelihood.getPlace().getTypes()).toString().contains("RESTAURANT")) { // Display only Restaurant
                        if (getContext() != null) { // Prevent application crash if switch too fast between map and list
                            placeList.add(placeLikelihood.getPlace());
                            addMarker(placeLikelihood.getPlace());
                        }
                    }
                }
            } else {
                Exception exception = task.getException();
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("ERROR", "Place not found: " + apiException.getMessage());
                }
            }
        });
    }

    // Convert vector image to bitmapDescriptor, here for marker icons on maps
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, boolean isEating) {
        Drawable vectorDrawable;
        if (!isEating)
            vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_restaurant_marker_yellow);
        else
            vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_restaurant_marker_green);

        Objects.requireNonNull(vectorDrawable).setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Add marker
    public void addMarker(Place place) {
        WorkmateHelper.getWorkmatesCollection()
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            // Check user is eating at this restaurant
                            if (WorkmateHelper.getStringInfoFrom(Constant.RESTAURANT_ID, document).equals(place.getId())) {
                                mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng())
                                        .icon(bitmapDescriptorFromVector(getContext(), true))
                                        .title(place.getName()));
                                return;
                            }
                        }

                        mGoogleMap.addMarker(new MarkerOptions().position(place.getLatLng())
                                .icon(bitmapDescriptorFromVector(getContext(), false))
                                .title(place.getName()));

                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
    }

    @Override
    public void getAutocompleteResult(Place place) {
        // Set marker for selected restaurant
        addMarker(place);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng())); // Move the camera on selected restaurant
    }

    private void clickOnMarker() {
        mGoogleMap.setOnInfoWindowClickListener(marker -> {

            LatLng latLon = marker.getPosition();
            //Cycle through places array
            for (Place place : placeList) {
                if (latLon.equals(place.getLatLng())) {
                    getPlaceDetails(place.getId());
                }
            }
        });
    }
    private double ratingFormatter(Place mPlace) {
        double rating;
        if (mPlace.getRating() != null)
            rating = mPlace.getRating();
        else rating = 0.0;

        return rating;
    }

    public String websiteFormatter(Uri websiteUri) {
        String website;

        if (websiteUri != null)
            website = websiteUri.toString();
        else website = getString(R.string.no_website_available);

        return website;
    }

    private void getPlaceDetails(String placeId){
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.RATING,
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.USER_RATINGS_TOTAL,
                Place.Field.RATING,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.PHOTO_METADATAS,
                Place.Field.UTC_OFFSET,
                Place.Field.ID,
                Place.Field.OPENING_HOURS);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);

        mPlacesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
            intent.putExtra(Constant.RESTAURANT_NAME, place.getName());
            intent.putExtra("address", place.getAddress());
            intent.putExtra("rating", ratingFormatter(place));
            intent.putExtra("picture", place.getPhotoMetadatas().get(0));
            intent.putExtra("phone", place.getPhoneNumber());
            intent.putExtra("website",websiteFormatter(place.getWebsiteUri()));
            intent.putExtra(Constant.RESTAURANT_ID, place.getId());

            startActivity(intent);


        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Log.e("ERROR", "Place not found: " + exception.getMessage());
            }
        });
    }
}
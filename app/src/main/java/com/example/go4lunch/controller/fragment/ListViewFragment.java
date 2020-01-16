package com.example.go4lunch.controller.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.Constant;
import com.example.go4lunch.R;
import com.example.go4lunch.controller.activities.RestaurantDetailsActivity;
import com.example.go4lunch.controller.recycler.ListViewRecyclerAdapter;
import com.example.go4lunch.controller.recycler.RecyclerHolderListener;
import com.example.go4lunch.data.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by Dutru Thomas on 06/09/2019.
 */
public class ListViewFragment extends Fragment implements DoSearch{
    private ListViewRecyclerAdapter mAdapter;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    private PlacesClient mPlacesClient;
    private PhotoMetadata mPhotoMetadata = null;
    private Place mPlace;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View listViewFragment = inflater.inflate(R.layout.fragment_list_view, container, false);
        mRecyclerView = listViewFragment.findViewById(R.id.fragment_list_view_recycler);

        // Initialize the SDK
        Places.initialize(Objects.requireNonNull(getContext()), BuildConfig.GoogleMapsApiKey);
        // Create a new Places client instance
        mPlacesClient = Places.createClient(Objects.requireNonNull(getContext()));
        // Location Services
        LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));

        configureRecyclerView();
        initPlaces();

        return listViewFragment;
    }

    // Click on item on listView, put the extra for restaurantDetails
    RecyclerHolderListener recyclerHolderListener = (viewHolder, item, pos) -> {
        Restaurant restaurant = (Restaurant) item;
        Intent intent = new Intent(getContext(), RestaurantDetailsActivity.class);
        intent.putExtra(Constant.RESTAURANT_NAME, restaurant.getName());
        intent.putExtra("address", restaurant.getAddress());
        intent.putExtra("rating", restaurant.getUserRating());
        intent.putExtra("picture", restaurant.getPhotoMetadata());
        intent.putExtra("phone", restaurant.getPhoneNumber());
        intent.putExtra("website", restaurant.getWebsiteUrl());
        intent.putExtra(Constant.RESTAURANT_ID, restaurant.getId());

        startActivity(intent);
    };

    private void configureRecyclerView() {
        mAdapter = new ListViewRecyclerAdapter(mRestaurantList, recyclerHolderListener, mPlacesClient);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation()); // Make line between item elements
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter.setListener(recyclerHolderListener);
    }

    private void initPlaces() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.TYPES,
                Place.Field.ID);
        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        Task<FindCurrentPlaceResponse> placeResponse = mPlacesClient.findCurrentPlace(request);
        placeResponse.addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                FindCurrentPlaceResponse response = task.getResult();
                assert response != null;

                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                    if (placeLikelihood.getPlace().getTypes().toString().contains("RESTAURANT")) { // Display only Restaurant
                        getRestaurantDetails(placeLikelihood.getPlace().getId());
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

    private void getRestaurantDetails(String placeId){
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
            mPlace = response.getPlace();

            // Construct the restaurant object
            Restaurant restaurant = new Restaurant(
                    nameFormatter(mPlace.getName()),
                    addressFormatter(),
                    distanceFormatter(SphericalUtil.computeDistanceBetween(MapFragment.mUserPosition, Objects.requireNonNull(mPlace.getLatLng()))),
                    userRatingTotalFormatter(mPlace.getUserRatingsTotal()),
                    openingHoursFormatter(),
                    websiteFormatter(mPlace.getWebsiteUri()),
                    mPlace.getPhoneNumber(),
                    mPlace.getId(),
                    isOpenFormatter(),
                    ratingFormatter(),
                    photoMetadataFormatter());

            mRestaurantList.add(restaurant);
            if (!mRestaurantList.isEmpty())
                mAdapter.notifyDataSetChanged(); // Refresh data for update the list for the recycler
            else
                Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                // Handle error with given status code.
                Log.e("ERROR", "Place not found: " + exception.getMessage());
            }
        });
    }

    // Use for display opening hours of current day
    public String displayOpeningHoursForCurrentDay(List<String> listOfOpeningHours) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String openingHoursForCurrentDay = null;

        switch (day) {
            case Calendar.MONDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(0);
                break;
            case Calendar.TUESDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(1);
                break;
            case Calendar.WEDNESDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(2);
                break;
            case Calendar.THURSDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(3);
                break;
            case Calendar.FRIDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(4);
                break;
            case Calendar.SATURDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(5);
                break;
            case Calendar.SUNDAY:
                openingHoursForCurrentDay = listOfOpeningHours.get(6);
                break;
            default:
                break;
        }
        return Objects.requireNonNull(openingHoursForCurrentDay).substring(openingHoursForCurrentDay.indexOf(":") + 2); // Return string without "day"
    }

    @Override
    public void getAutocompleteResult(Place place) {
        mPlace = place;
        mRestaurantList.clear(); // Clear list before display result

        if (mPlace.getLatLng() == null) {
            Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();
            return;
        }
        Restaurant restaurant = new Restaurant(
                nameFormatter(mPlace.getName()),
                addressFormatter(),
                distanceFormatter(SphericalUtil.computeDistanceBetween(MapFragment.mUserPosition, Objects.requireNonNull(mPlace.getLatLng()))),
                userRatingTotalFormatter(mPlace.getUserRatingsTotal()),
                openingHoursFormatter(),
                websiteFormatter(mPlace.getWebsiteUri()),
                mPlace.getPhoneNumber(),
                mPlace.getId(),
                isOpenFormatter(),
                ratingFormatter(),
                photoMetadataFormatter());

        mRestaurantList.add(restaurant);
        if (!mRestaurantList.isEmpty())
            mAdapter.notifyDataSetChanged();
        else
            Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();
    }

    public String nameFormatter(String name) {
        String formattedName;
        int dashIndex = name.indexOf("-");
        int decimalPointIndex = name.indexOf(",");

        if (dashIndex > -1 || decimalPointIndex > -1) { // "-" or "," is present
            if (dashIndex > -1 && decimalPointIndex > -1) { // "-" and "," are present
                formattedName = name.substring(0, Math.min(dashIndex, decimalPointIndex)); // The first character "-" or "," are present put the end of returned string
                return formattedName;
            }
            if (decimalPointIndex > -1) { // only "," is present
                formattedName = name.substring(0 , decimalPointIndex); // formatted string return up to ","
            } else {
                formattedName = name.substring(0, dashIndex); // formatted string return up to "-"
            }
        } else {
            formattedName = name; // formatted string return string without changed
        }
        return formattedName;
    }

    private String addressFormatter() {
        String address;
        if (mPlace.getAddressComponents() != null)
            address = "" + mPlace.getAddressComponents().asList().get(0).getName() + ", " + mPlace.getAddressComponents().asList().get(1).getName() + ", " + mPlace.getAddressComponents().asList().get(2).getName();
        else address = getString(R.string.no_address_found);
        return address;
    }

    private PhotoMetadata photoMetadataFormatter() {
        if (mPlace.getPhotoMetadatas() != null)
            mPhotoMetadata = mPlace.getPhotoMetadatas().get(0);
        return mPhotoMetadata;
    }

    public String distanceFormatter(double distanceFrom) {
        DecimalFormat df = new DecimalFormat("###"); // Format distance to avoid : .0 after the distance
        return df.format(distanceFrom) + " m";
    }

    public String websiteFormatter(Uri websiteUri) {
        String website;

        if (websiteUri != null)
            website = websiteUri.toString();
        else website = getString(R.string.no_website_available);

        return website;
    }

    private String openingHoursFormatter() {
        String openingHours;

        if (mPlace.getOpeningHours() != null)
            openingHours = displayOpeningHoursForCurrentDay(mPlace.getOpeningHours().getWeekdayText());
        else openingHours = "";

        return openingHours;
    }

    public String userRatingTotalFormatter(Integer userRatingTotal) {
        String rating = "(0)";
        if (userRatingTotal != null)
            rating = "(" + userRatingTotal.toString() + ")";
        return rating;
    }

    private boolean isOpenFormatter() {
        boolean isOpen;

        if (mPlace.isOpen() != null)
            isOpen = mPlace.isOpen();
        else isOpen = false;

        return isOpen;
    }

    private double ratingFormatter() {
        double rating;
        if (mPlace.getRating() != null)
            rating = mPlace.getRating();
        else rating = 0.0;

        return rating;
    }
}

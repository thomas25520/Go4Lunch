package com.example.go4lunch.controller.fragment;

import android.content.Intent;
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

import com.example.go4lunch.R;
import com.example.go4lunch.api.ApiKeyManager;
import com.example.go4lunch.controller.activities.RestaurantDetails;
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
    private ApiKeyManager mApiKeyManager = new ApiKeyManager();

    private String mAddress, mDistance, mWebsite, mOpeningHours, mUserRatingTotal;
    private PhotoMetadata mPhotoMetadata;
    private boolean mIsOpen;
    private double mRating;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View listViewFragment = inflater.inflate(R.layout.fragment_list_view, container, false);
        mRecyclerView = listViewFragment.findViewById(R.id.fragment_list_view_recycler);

        // Initialize the SDK
        Places.initialize(Objects.requireNonNull(getContext()), mApiKeyManager.getGoogleMapsApiKey());
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
        Intent intent = new Intent(getContext(), RestaurantDetails.class);
        intent.putExtra("restaurantName", restaurant.getName());
        intent.putExtra("address", restaurant.getAddress());
        intent.putExtra("rating", restaurant.getUserRating());
        intent.putExtra("picture", restaurant.getPhotoMetadata());
        intent.putExtra("rating", restaurant.getUserRating());
        intent.putExtra("phone", restaurant.getPhoneNumber());
        intent.putExtra("website", restaurant.getWebsiteUrl());
        intent.putExtra("restaurantId", restaurant.getId());

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
            Place place = response.getPlace();

            // Format the address
            mAddress = "" + place.getAddressComponents().asList().get(0).getName() + ", " + place.getAddressComponents().asList().get(1).getName() + ", " + place.getAddressComponents().asList().get(2).getName();

            // Get the photo metadata.
            mPhotoMetadata = null;
            if (place.getPhotoMetadatas() != null)
                mPhotoMetadata = place.getPhotoMetadatas().get(0);

            // Get the distance from a restaurant
            double distanceFrom = SphericalUtil.computeDistanceBetween(MapFragment.mUserPosition, Objects.requireNonNull(place.getLatLng()));
            DecimalFormat df = new DecimalFormat("###"); // Format distance to avoid : .0 after the distance
            mDistance = df.format(distanceFrom) + " m";

            // Get the website from a restaurant
            if (place.getWebsiteUri() != null) { mWebsite = place.getWebsiteUri().toString();
            } else { mWebsite = getString(R.string.no_website_available); }

            // Get the opening hours of the day
            if (place.getOpeningHours() != null) {
                mOpeningHours = displayOpeningHoursForCurrentDay(place.getOpeningHours().getWeekdayText());
            } else {
                mOpeningHours = "";
            }

            // Get user rating total
            if (place.getUserRatingsTotal() != null) {
                mUserRatingTotal = "(" + place.getUserRatingsTotal().toString() + ")";
            } else {
                mUserRatingTotal = "(0)";
            }

            // Get restaurant is open
            if (place.isOpen() != null) {
                mIsOpen = place.isOpen();
            } else {
                mIsOpen = false;
            }

            // Place rating
            if (place.getRating() != null) {
                mRating = place.getRating();
            } else {
                mRating = 0.0;
            }

            // Construct the restaurant object
            Restaurant restaurant = new Restaurant(
                    place.getName(),
                    mAddress,
                    mDistance,
                    mUserRatingTotal,
                    mOpeningHours,
                    mWebsite,
                    place.getPhoneNumber(),
                    place.getId(),
                    mIsOpen,
                    mRating,
                    mPhotoMetadata);

            mRestaurantList.add(restaurant);
            if (!mRestaurantList.isEmpty())
                mAdapter.notifyDataSetChanged(); // Refresh data for update the list for the recycler
            else
                Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e("ERROR", "Place not found: " + exception.getMessage());
            }
        });
    }

    // Use for display opening hours of current day
    private String displayOpeningHoursForCurrentDay(List<String> listOfOpeningHours) {
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
        // FIXME: 18/12/2019 DUplicate code, see how to reduce
        Toast.makeText(getContext(), place.getId(), Toast.LENGTH_LONG).show();
        // maj la liste avec le seul item
        mRestaurantList.clear();

        // Format the address
        mAddress = "" + place.getAddressComponents().asList().get(0).getName() + ", " + place.getAddressComponents().asList().get(1).getName() + ", " + place.getAddressComponents().asList().get(2).getName();

        // Get the photo metadata.
        mPhotoMetadata = null;
        if (place.getPhotoMetadatas() != null)
            mPhotoMetadata = place.getPhotoMetadatas().get(0);

        // Get the distance from a restaurant
        double distanceFrom = SphericalUtil.computeDistanceBetween(MapFragment.mUserPosition, Objects.requireNonNull(place.getLatLng()));
        DecimalFormat df = new DecimalFormat("###"); // Format distance to avoid : .0 after the distance
        mDistance = df.format(distanceFrom) + " m";

        // Get the website from a restaurant
        if (place.getWebsiteUri() != null) { mWebsite = place.getWebsiteUri().toString();
        } else { mWebsite = getString(R.string.no_website_available); }

        // Get the opening hours of the day
        if (place.getOpeningHours() != null) {
            mOpeningHours = displayOpeningHoursForCurrentDay(place.getOpeningHours().getWeekdayText());
        } else {
            mOpeningHours = "";
        }

        // Get user rating total
        if (place.getUserRatingsTotal() != null) {
            mUserRatingTotal = "(" + place.getUserRatingsTotal().toString() + ")";
        } else {
            mUserRatingTotal = "(0)";
        }

        // Get restaurant is open
        if (place.isOpen() != null) {
            mIsOpen = place.isOpen();
        } else {
            mIsOpen = false;
        }

        // Place rating
        if (place.getRating() != null) {
            mRating = place.getRating();
        } else {
            mRating = 0.0;
        }

        // Construct the restaurant object
        Restaurant restaurant = new Restaurant(
                place.getName(),
                mAddress,
                mDistance,
                mUserRatingTotal,
                mOpeningHours,
                mWebsite,
                place.getPhoneNumber(),
                place.getId(),
                mIsOpen,
                mRating,
                mPhotoMetadata);

        mRestaurantList.add(restaurant);
        if (!mRestaurantList.isEmpty())
            mAdapter.notifyDataSetChanged(); // Refresh data for update the list for the recycler
        else
            Toast.makeText(getContext(), R.string.no_restaurant_found, Toast.LENGTH_LONG).show();
    }
}

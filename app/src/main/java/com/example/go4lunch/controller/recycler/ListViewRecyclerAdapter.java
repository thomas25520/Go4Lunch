package com.example.go4lunch.controller.recycler;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.data.Restaurant;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */

public class ListViewRecyclerAdapter extends RecyclerView.Adapter<ListViewViewHolder> {
    private List<Restaurant> mRestaurantList;
    private ListViewRecyclerHolderListener mHolderListener;
    private PlacesClient mPlacesClient;

    public ListViewRecyclerAdapter (List<Restaurant> restaurantList, ListViewRecyclerHolderListener listener, PlacesClient placesClient) {
        mPlacesClient = placesClient;
        mRestaurantList = restaurantList;
        mHolderListener = listener;
    }

    @NonNull
    @Override
    public ListViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_list_view_item,parent,false);
        return new ListViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewViewHolder holder, int position) {
        Restaurant restaurant = mRestaurantList.get(position);

        holder.mRestaurantAddress.setText(restaurant.getAddress());
        holder.mRestaurantName.setText(restaurant.getName());
        holder.mRestaurantOpenHours.setText(restaurant.getHours());

        if (restaurant.isOpening()) { // Display if restaurant is open or not
            holder.mRestaurantIsOpen.setText(R.string.open);
            holder.mRestaurantIsOpen.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_green));
        } else {
            holder.mRestaurantIsOpen.setText(R.string.close);
            holder.mRestaurantIsOpen.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.color_red));
        }
        holder.mRestaurantDistance.setText(restaurant.getDistance());
        holder.mRestaurantNumberOfPersonVote.setText(restaurant.getUserVote());

        if (restaurant.getPhotoMetadata() == null) { // Handle error if no pictureUrl and display a fix default image
            holder.mRestaurantPicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_no_camera));
        } else {
            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(restaurant.getPhotoMetadata())
                    .build();
            mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                holder.mRestaurantPicture.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e("ERROR", "Place not found: " + exception.getMessage());
                }
            });
        }

        holder.mRestaurantRatingBar.setRating((float) (restaurant.getUserRating() *3 )/5); // Rating() * 3 / 5 : To have same proportion of rating like 5 star, in 3 stars

        holder.itemView.setOnClickListener(v -> mHolderListener.onItemClicked(holder, restaurant, position));
    }

    public void setListener(ListViewRecyclerHolderListener listener) {
        this.mHolderListener = listener;
    }

    @Override
    public int getItemCount() {
        return mRestaurantList.size();
    }
}

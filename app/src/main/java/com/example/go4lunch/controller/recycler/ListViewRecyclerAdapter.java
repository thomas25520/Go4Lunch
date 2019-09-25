package com.example.go4lunch.controller.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.controller.data.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class ListViewRecyclerAdapter extends RecyclerView.Adapter<ListViewViewHolder> {

    private List<Restaurant> mRestaurantList;
    private ListViewRecyclerHolderListener mHolderListener;


    public ListViewRecyclerAdapter (List<Restaurant> restaurantList, ListViewRecyclerHolderListener listener) {
        this.mRestaurantList = restaurantList;
        this.mHolderListener = listener;
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
        holder.mRestaurantIsOpen.setText(restaurant.getOpening());
        holder.mRestaurantOrigin.setText(restaurant.getOrigin());
        holder.mRestaurantDistance.setText(restaurant.getDistance());
        holder.mRestaurantNumberOfPersonVote.setText(restaurant.getUserVote());

        Picasso.get().load(mRestaurantList.get(position).getPictureUrl()).into(holder.mRestaurantPicture);
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

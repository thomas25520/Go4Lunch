package com.example.go4lunch.controller.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class ListViewViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fragment_list_view_item_restaurant_name) TextView mRestaurantName;
    @BindView(R.id.fragment_list_view_item_restaurant_address) TextView mRestaurantAddress;
    @BindView(R.id.fragment_list_view_item_restaurant_hours) TextView mRestaurantOpenHours;
    @BindView(R.id.fragment_list_view_item_restaurant_is_open) TextView mRestaurantIsOpen;
    @BindView(R.id.fragment_list_view_item_distance_from_the_restaurant) TextView mRestaurantDistance;
    @BindView(R.id.fragment_list_view_item_number_of_person_vote) TextView mRestaurantNumberOfPersonVote;
    @BindView(R.id.fragment_list_view_item_rating_bar) RatingBar mRestaurantRatingBar;
    @BindView(R.id.fragment_list_view_item_restaurant_picture) ImageView mRestaurantPicture;

    public ListViewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

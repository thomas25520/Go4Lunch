package com.example.go4lunch.controller.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsViewHolder  extends RecyclerView.ViewHolder{
    @BindView(R.id.fragment_workmates_item_restaurant_name) TextView mRestaurantName;
    @BindView(R.id.fragment_workmates_item_user_name) TextView mUserName;
    @BindView(R.id.fragment_workmates_item_user_picture) ImageView mUserPicture;
    @BindView(R.id.fragment_workmates_item_is_eating) TextView mIsEating;

    public RestaurantDetailsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
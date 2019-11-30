package com.example.go4lunch.controller.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dutru Thomas on 13/09/2019.
 */
public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.fragment_workmates_item_restaurant_name) TextView mRestaurantName;
    @BindView(R.id.fragment_workmates_item_user_name) TextView mUserName;
    @BindView(R.id.fragment_workmates_item_is_eating) TextView mIsEating;
    @BindView(R.id.fragment_workmates_item_user_picture) ImageView mUserPicture;

    public WorkmatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

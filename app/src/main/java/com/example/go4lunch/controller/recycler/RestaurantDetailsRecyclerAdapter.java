package com.example.go4lunch.controller.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.data.Workmate;

import java.util.List;

public class RestaurantDetailsRecyclerAdapter extends RecyclerView.Adapter<RestaurantDetailsViewHolder>{
    private List<Workmate> mWorkmateList;

    public  RestaurantDetailsRecyclerAdapter (List<Workmate> workmateList) {
        mWorkmateList = workmateList;
    }

    @NonNull
    @Override
    public RestaurantDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_workmates_item, parent,false);
        return new RestaurantDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDetailsViewHolder holder, int position) {
        Workmate workmate = mWorkmateList.get(position);
        holder.mUserName.setText(workmate.getName());
        holder.mRestaurantName.setText(""); // No display useless restaurant name.
        WorkmatesRecyclerAdapter.roundImageDisplays(holder.mUserPicture, mWorkmateList.get(position).getPictureUrl());
    }

    @Override
    public int getItemCount() {
        return mWorkmateList.size();
    }
}

package com.example.go4lunch.controller.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.controller.data.Workmates;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Dutru Thomas on 13/09/2019.
 */
public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {
    private List<Workmates> mWorkmatesList;

    public WorkmatesRecyclerAdapter (List<Workmates> workmatesList) {
        this.mWorkmatesList = workmatesList;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_workmates_item, parent,false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        Workmates workmates = mWorkmatesList.get(position);

        holder.mUserName.setText(workmates.getUserFirstName());
        holder.mIsEating.setText(workmates.getUserIsEating());
        holder.mRestaurantOrigin.setText(workmates.getRestaurantOrigin());
        holder.mRestaurantName.setText(workmates.getRestaurantName());

        roundImageDisplays(holder.mUserPicture, mWorkmatesList.get(position).getUserPictureUrl());
    }

    private void roundImageDisplays (ImageView imageView, String urlPictureToDisplay) {
        Transformation transformation = new RoundedTransformationBuilder()
//                .borderColor(Color.BLACK)
//                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(true)
                .build();

        Picasso.get()
                .load(urlPictureToDisplay)
                .fit()
                .transform(transformation)
                .into(imageView);
    }


    @Override
    public int getItemCount() {
        return mWorkmatesList.size();
    }
}

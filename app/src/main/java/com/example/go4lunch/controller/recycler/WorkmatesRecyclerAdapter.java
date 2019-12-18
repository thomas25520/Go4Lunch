package com.example.go4lunch.controller.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.data.Workmate;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Dutru Thomas on 13/09/2019.
 */
public class WorkmatesRecyclerAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {
    private List<Workmate> mWorkmateList;

    public WorkmatesRecyclerAdapter (List<Workmate> workmateList) {
        this.mWorkmateList = workmateList;
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
        Workmate workmate = mWorkmateList.get(position);

        holder.mUserName.setText(workmate.getName());
        if (workmate.isEating())
            holder.mIsEating.setText(R.string.is_eating);
        else
            holder.mIsEating.setText("");

        holder.mRestaurantName.setText(workmate.getRestaurantName());

        roundImageDisplays(holder.mUserPicture, mWorkmateList.get(position).getPictureUrl());
    }

    public static void roundImageDisplays (ImageView imageView, String urlPictureToDisplay) {
        Transformation transformation = new RoundedTransformationBuilder()
//                .borderColor(Color.BLACK)
//                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(true)
                .build();

        if(urlPictureToDisplay != null)
            Picasso.get()
                    .load(urlPictureToDisplay)
                    .fit()
                    .transform(transformation)
                    .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mWorkmateList.size();
    }
}

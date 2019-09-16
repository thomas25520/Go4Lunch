package com.example.go4lunch.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.controller.activities.RestaurantDetails;
import com.example.go4lunch.controller.data.Restaurant;
import com.example.go4lunch.controller.recycler.ListViewRecyclerAdapter;
import com.example.go4lunch.controller.recycler.ListViewRecyclerHolderListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dutru Thomas on 06/09/2019.
 */
public class ListViewFragment extends Fragment {

    private ListViewRecyclerAdapter mAdapter;
    private List<Restaurant> mRestaurantList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View listViewFragment = inflater.inflate(R.layout.fragment_list_view, container, false);
        mRecyclerView = listViewFragment.findViewById(R.id.fragment_list_view_recycler);

        configureRecyclerView();
        initData();

        return listViewFragment;
    }

    private void initData() {
        Restaurant restaurant = new Restaurant("Thommy","Français", "sombacour", "OPEN", "21:00","0", "0", "https://upload.wikimedia.org/wikipedia/commons/c/c9/Sombacour_-_vue_1.JPG");
        mRestaurantList.add(restaurant);
        restaurant = new Restaurant("Les granges de Fred","Français", "sombacour", "OPEN", "18:30", "0", "0", "https://upload.wikimedia.org/wikipedia/commons/3/3b/Sombacour_-_Mont-calvaire_-_vue_2.JPG");
        mRestaurantList.add(restaurant);
        restaurant = new Restaurant("Les Délices de Manamis","International", "sombacour", "OPEN", "18:30", "450", "36", "https://cdn-s-www.estrepublicain.fr/images/3A72D60E-2D51-49B6-BB8F-34989E66179F/LER_22/incendie-au-lieu-dit-le-souillot-a-la-chapelle-d-huin-1522764351.jpg");
        mRestaurantList.add(restaurant);
        restaurant = new Restaurant("Le kosovar","kosovo", "sombacour", "OPEN", "19:00","250", "6", "https://www.communes.com/images/orig/franche-comte/doubs/sombacour_25520/Sombacour_11027_calvaire.jpg");
        mRestaurantList.add(restaurant);
    }

    private void configureRecyclerView() {
        ListViewRecyclerHolderListener listViewRecyclerHolderListener= (viewHolder, item, pos) -> {
            Restaurant restaurant = (Restaurant) item;
            Intent intent = new Intent(getContext(), RestaurantDetails.class);

            intent.putExtra("name", restaurant.getName());
            intent.putExtra("address", restaurant.getAddress());
            intent.putExtra("origin", restaurant.getOrigin());
            intent.putExtra("pictureUrl", restaurant.getPictureUrl());

            startActivity(intent);
        };

        mAdapter = new ListViewRecyclerAdapter(mRestaurantList, listViewRecyclerHolderListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager) mLayoutManager).getOrientation()); // Make line between item elements

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter.setListener(listViewRecyclerHolderListener);
    }
}

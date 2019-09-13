package com.example.go4lunch.controller.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.controller.data.Workmates;
import com.example.go4lunch.controller.recycler.WorkmatesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dutru Thomas on 06/09/2019.
 */
public class WorkmatesFragment extends Fragment {

    private WorkmatesRecyclerAdapter mAdapter;
    private List<Workmates> mWorkmatesList = new ArrayList<>();
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View workmatesFragment = inflater.inflate(R.layout.fragment_workmates,container, false);
        mRecyclerView = workmatesFragment.findViewById(R.id.fragment_workmates_recycler);

        configureRecyclerView();
        initData();

        return workmatesFragment;
    }

    private void initData() {
        Workmates workmates = new Workmates("Thomas", "is eating","French", "Les granges de fred", "https://resize-europe1.lanmedia.fr/r/622,311,forcex,center-middle/img/var/europe1/storage/images/europe1/international/selon-trump-lassaillant-de-new-york-est-une-personne-tres-malade-3479963/44980487-1-fre-FR/Selon-Trump-l-assaillant-de-New-York-est-une-personne-tres-malade.jpg");
        mWorkmatesList.add(workmates);
    }

    private void configureRecyclerView() {
        mAdapter = new WorkmatesRecyclerAdapter(mWorkmatesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }
}

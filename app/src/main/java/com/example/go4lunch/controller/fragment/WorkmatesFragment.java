package com.example.go4lunch.controller.fragment;

import android.os.Bundle;
import android.util.Log;
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
import com.example.go4lunch.api.UserHelper;
import com.example.go4lunch.controller.data.Workmates;
import com.example.go4lunch.controller.recycler.WorkmatesRecyclerAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        UserHelper.getUsersCollection()
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Workmates workmates = new Workmates(
                                    getInfoFrom("userName", document),
                                    getInfoFrom("eating", document), // FIXME: 02/10/2019 : Convert all to boolean
                                    "", // FIXME: 02/10/2019 : add restaurant info when available
                                    "", // FIXME: 02/10/2019 : add restaurant info when available
                                    getInfoFrom("userUrlProfilePicture", document)
                            );
                            mWorkmatesList.add(workmates);
                            mAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d("ERROR", "Error getting documents: ", task.getException());
                    }
                });
    }

    private String getInfoFrom(String information, QueryDocumentSnapshot document) {
        return Objects.requireNonNull(document.getData().get(information)).toString();
    }

    private void configureRecyclerView() {
        mAdapter = new WorkmatesRecyclerAdapter(mWorkmatesList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation()); // Make line between item elements

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }
}

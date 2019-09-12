package com.example.go4lunch.controller.recycler;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Dutru Thomas on 12/09/2019.
 */
public interface ListViewRecyclerHolderListener<T, VH extends RecyclerView.ViewHolder> {
    void onItemClicked(VH vh, T item, int pos);
}
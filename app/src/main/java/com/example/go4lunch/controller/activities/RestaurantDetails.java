package com.example.go4lunch.controller.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dutru Thomas on 05/09/2019.
 *
 */
public class RestaurantDetails extends AppCompatActivity {

    @BindView(R.id.activity_restaurant_details_name) TextView mRestaurantName;
    @BindView(R.id.activity_restaurant_details_address) TextView mRestaurantAddress;
    @BindView(R.id.activity_restaurant_details_origin) TextView mRestaurantOrigin;
    @BindView(R.id.activity_restaurant_details_picture) ImageView mRestaurantPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this); // apply the configuration with butterKnife for use @BindView, always use after setContentView
        initViews();
    }

    private void initViews() {
        mRestaurantName.setText(getIntent().getStringExtra("name"));
        mRestaurantAddress.setText(getIntent().getStringExtra("address"));
        mRestaurantOrigin.setText(getIntent().getStringExtra("origin"));

        Picasso.get().load(getIntent().getStringExtra("pictureUrl")).into(mRestaurantPicture);
    }

    // Handle menu button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

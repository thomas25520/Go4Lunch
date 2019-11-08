package com.example.go4lunch.controller.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicBoolean;

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

    @BindView(R.id.activity_restaurant_details_participation_btn) FloatingActionButton mParticipationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this); // apply the configuration with butterKnife for use @BindView, always use after setContentView
        configureFloatingActionBtn();
        initViews();

    }

    private void initViews() {
        mRestaurantName.setText(getIntent().getStringExtra("name"));
        mRestaurantAddress.setText(getIntent().getStringExtra("address"));
        mRestaurantOrigin.setText(getIntent().getStringExtra("origin"));

        Picasso.get().load(getIntent().getStringExtra("pictureUrl")).into(mRestaurantPicture);
    }

    // Handle back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureFloatingActionBtn() {
        AtomicBoolean participationBtnState = new AtomicBoolean(false);
        // TODO: 16/09/2019 When API user is implemented dont forget to save btn position in function of user participation
        // Give user participation states to the lunch
        mParticipationBtn.setOnClickListener(v -> {

                    if (participationBtnState.get()) {
                        mParticipationBtn.setImageResource(R.drawable.ic_check);
                        participationBtnState.set(false);
                        Snackbar.make(v, R.string.participation_btn_info_user_false, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        // FIXME: 16/09/2019 Here delete user from the list of participant to the lunch
                    }
                    else {
                        mParticipationBtn.setImageResource(R.drawable.ic_cancel);
                        participationBtnState.set(true);
                        Snackbar.make(v, R.string.participation_btn_info_user_true, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        // FIXME: 16/09/2019 Here add user to the list of participant to the lunch

                    }
        });
    }
}

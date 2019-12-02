package com.example.go4lunch.controller.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    @BindView(R.id.activity_restaurant_details_picture) ImageView mRestaurantPicture;
    @BindView(R.id.activity_restaurant_details_rating_bar) RatingBar mRestaurantRatingBar;
    @BindView(R.id.activity_restaurant_details_participation_btn) FloatingActionButton mParticipationBtn;
    @BindView(R.id.activity_restaurant_details_web_logo) ImageView mRestaurantWebsiteUrl;
    @BindView(R.id.activity_restaurant_details_call_logo) ImageView mRestaurantCallNumber;
    @BindView(R.id.activity_restaurant_details_like_logo) ImageView mRestaurantLike;

    private PlacesClient mPlacesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this); // apply the configuration with butterKnife for use @BindView, always use after setContentView
        actionParticipationBtn();
        initViews();

    }

    private void initViews() {
        Places.initialize(this, getString(R.string.google_api_key));
        // Create a new Places client instance
        mPlacesClient = Places.createClient(this);

        mRestaurantName.setText(getIntent().getStringExtra("name"));
        mRestaurantAddress.setText(getIntent().getStringExtra("address"));
        double restaurantRating = getIntent().getDoubleExtra("rating", 0);
        mRestaurantRatingBar.setRating((float)restaurantRating *3 /5);

        PhotoMetadata restaurantPicture = getIntent().getParcelableExtra("picture");
        if (restaurantPicture == null) { // Handle error if no pictureUrl and display a fix default image
            mRestaurantPicture.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_no_camera));
        } else {
            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(restaurantPicture)
                    .build();
            mPlacesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                mRestaurantPicture.setImageBitmap(bitmap);
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e("ERROR", "Place not found: " + exception.getMessage());
                }
            });
        }

        // Phone intent to set phone number of restaurant.
        String phone;
        if (getIntent().getStringExtra("phone") != null) {
            phone = getIntent().getStringExtra("phone");
            Intent intentForCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));

            mRestaurantCallNumber.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_dialog_call_title)
                    .setMessage(getIntent().getStringExtra("phone"))
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.call, (dialog, which) -> startActivity(intentForCall))
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.close, null)
                    .show());
        } else {
            mRestaurantCallNumber.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle(R.string.sorry)
                    .setMessage(getString(R.string.no_phone_number_found))
                    .setNegativeButton(R.string.close, null)
                    .show());
        }

        if (getIntent().getStringExtra("website").contains(getString(R.string.no_website_available))) {
            mRestaurantWebsiteUrl.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle(R.string.sorry)
                    .setMessage(getIntent().getStringExtra("website"))
                    .setNegativeButton(R.string.close, null)
                    .show());
        } else {
            Intent intentForWebsite = new Intent(RestaurantDetails.this, WebViewActivity.class);
            intentForWebsite.putExtra("website",getIntent().getStringExtra("website")); // Pass intent again for display on WebViewActivity.

            mRestaurantWebsiteUrl.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle(R.string.go_to_website)
                    .setMessage(getIntent().getStringExtra("website"))
                    .setPositiveButton(R.string.go, (dialog, which) -> startActivity(intentForWebsite))
                    .setNegativeButton(R.string.close, null)
                    .show());
        }

        // Redirect user on google maps page, user can write a notice and vote.
        mRestaurantLike.setOnClickListener(v -> {
            String linkForGmToLike = "https://www.google.com/maps/place/?q=place_id:" + getIntent().getStringExtra("restaurantId");
            Intent intentForWebsite = new Intent(RestaurantDetails.this, WebViewActivity.class);
            intentForWebsite.putExtra("website", linkForGmToLike); // Pass intent again for display on WebViewActivity.
            startActivity(intentForWebsite);
        });
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

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    private void actionParticipationBtn() {
        AtomicBoolean participationBtnState = new AtomicBoolean(false);
        // TODO: 02/12/2019 use current user eating state on db to set state of participationBtn and remove AtomicBoolean
        // Give user participation states to the lunch
        mParticipationBtn.setOnClickListener(v -> {
            if (participationBtnState.get()) {
                mParticipationBtn.setImageResource(R.drawable.ic_check);
                participationBtnState.set(false);
                Snackbar.make(v, R.string.participation_btn_info_user_false, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // TODO: 02/12/2019 Delete user on list for recycler
                // TODO: 02/12/2019 write change state on DB
                WorkmateHelper.updateIsWorkmateEating(getCurrentUser().getEmail(),false); // Change eating status to false on DB
                // TODO: 02/12/2019 Delete restaurantId on DB
                WorkmateHelper.updateWorkmateRestaurantId("",getCurrentUser().getEmail()); // Del restaurant id on user in DB
            } else {
                String restaurantId = getIntent().getStringExtra("restaurantId");

                mParticipationBtn.setImageResource(R.drawable.ic_cancel);
                participationBtnState.set(true);
                Snackbar.make(v, R.string.participation_btn_info_user_true, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // TODO: 02/12/2019 Get all user have eating true and add it to a list for recycler
                // TODO: 02/12/2019 write change state on DB
                WorkmateHelper.updateIsWorkmateEating(getCurrentUser().getEmail(),true); // Change eating status to true on DB
                // TODO: 02/12/2019 write restaurant ID on user in DB when participate
                WorkmateHelper.updateWorkmateRestaurantId(restaurantId,getCurrentUser().getEmail()); // Save restaurant od on user on DB
            }
        });
    }
}
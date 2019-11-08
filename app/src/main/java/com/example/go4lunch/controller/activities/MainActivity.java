package com.example.go4lunch.controller.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.go4lunch.R;
import com.example.go4lunch.controller.fragment.ListViewFragment;
import com.example.go4lunch.controller.fragment.MapFragment;
import com.example.go4lunch.controller.fragment.WorkmatesFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Objects;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private TextView mUserName, mUserMailAddress;
    private ImageView mUserProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this); // allow butterKnife on activity

        setToolbar();
        configureNavigationDrawer();
        initBottomNavigationView();

        if (isCurrentUserLogged())
            updateUserInfoWhenConnecting();
    }

    // Set Toolbar
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.i_m_hungry);
    }

    // Configure NavigationDrawer
    private void configureNavigationDrawer() {
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        View headerView = navigationView.getHeaderView(0);

        mDrawerLayout = findViewById(R.id.activity_main_drawer_layout);

        // headerView is used to precise is NavDrawer header view, not principal view. Without crash because mUsers are null.
        mUserName = headerView.findViewById(R.id.activity_main_nav_drawer_name);
        mUserMailAddress = headerView.findViewById(R.id.activity_main_nav_drawer_email_address);
        mUserProfilePicture = headerView.findViewById(R.id.activity_main_nav_drawer_user_profile_picture);

        navigationView.setNavigationItemSelectedListener(this);
    }

    // Disconnect user, use on NavDrawer, LogOut button click
    public void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class)); // Start LoginActivity after logOut
                    Toast.makeText(getApplicationContext(), R.string.log_out_message, Toast.LENGTH_LONG).show();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu, menu);
        return true;
    }

    // Selected item on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Hamburger menu
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.activity_main_toolbar_search_btn:
                Toast.makeText(this, R.string.function_under_development, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Selected item on Navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle Navigation Item Click
        switch (item.getItemId()) {
            case R.id.activity_main_drawer_lunch:
                Toast.makeText(this, R.string.function_under_development, Toast.LENGTH_LONG).show();
                break;
            case R.id.activity_main_drawer_logout:
                logOut();
                break;
            case R.id.activity_main_drawer_settings:
                Toast.makeText(this, R.string.function_under_development, Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_LONG).show();
                break;
        }
        this.mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initBottomNavigationView() {
        BottomNavigationView navigation = findViewById(R.id.activity_main_bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        showFragment(new MapFragment()); // Start with this fragment, Without, no fragment display at stat.
    }

    // Listener for item selected BottomNavigation
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.map_view:
                showFragment(new MapFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.i_m_hungry);
                return true;
            case R.id.list_view:
                showFragment(new ListViewFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.i_m_hungry);
                return true;
            case R.id.workmates:
                showFragment(new WorkmatesFragment());
                Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.available_workmates);
                return true;
        }
        return false;
    };

    // Display fragment
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.activity_main_frame_layout, fragment)
                .commit();
    }

    // Get user connected info with FireBaseUI
    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    // Verify user is connected
    protected Boolean isCurrentUserLogged() { return (this.getCurrentUser() != null); }

    private void updateUserInfoWhenConnecting() {

        if (this.getCurrentUser() != null){

//            Get picture URL from Firebase
            if (this.getCurrentUser().getPhotoUrl() != null) {
                imageCircleTransformationAndDisplayWithPicasso(mUserProfilePicture,getCurrentUser().getPhotoUrl());
            }

            if (getCurrentUser().getEmail() != null)
                mUserMailAddress.setText(getCurrentUser().getEmail());

            if (getCurrentUser().getDisplayName() != null)
                mUserName.setText(getCurrentUser().getDisplayName());
        }
    }

    /**
     * used to display the profile picture of a user connected with firebaseUI like facebook, twitter ...
     * @param imageView Image view where you would like to display the picture
     * @param urlPictureToDisplay Url of the picture in Uri type
     */
    private void imageCircleTransformationAndDisplayWithPicasso (ImageView imageView, Uri urlPictureToDisplay) {
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
}
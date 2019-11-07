package com.example.go4lunch.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.api.UserHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private String mUserName, mUserMailAddress, mUserProfilePicture;

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this); // allow butterKnife on activity
    }

    @OnClick(R.id.activity_login_connect_btn_mail)
    public void onClickMailButton() {
        // Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivity(new AuthUI.IdpConfig.EmailBuilder().build());
    }

    @OnClick(R.id.activity_login_connect_btn_facebook)
    public void onClickFacebookButton() {
        Toast.makeText(this, "Under developing", Toast.LENGTH_LONG).show();
//  TODO: 30/09/2019 : Repair Login with Facebook : developer error to fix.
//        this.startSignInActivity(new AuthUI.IdpConfig.FacebookBuilder().build());
    }

    @OnClick(R.id.activity_login_connect_btn_google)
    public void onClickGoogleButton() {
        this.startSignInActivity(new AuthUI.IdpConfig.GoogleBuilder().build());
    }

    @OnClick(R.id.activity_login_connect_btn_twitter)
    public void onClickTwitterButton() {
        this.startSignInActivity(new AuthUI.IdpConfig.TwitterBuilder().build());
    }

    /**
     * Use to create sign in activity from Firebase
     * @param builder builder for login provider
     */
    private void startSignInActivity(AuthUI.IdpConfig builder) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(Collections.singletonList(builder)) // Set several providers
                        .setIsSmartLockEnabled(true, true)
                        .build(),
                RC_SIGN_IN);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                Toast.makeText(this,getString(R.string.connection_succeed), Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class)); // Start main activity if log succeed

                getUserInfoFromFirebaseAuth(); // Get user info from firebase auth
                UserHelper.createUser(mUserName, mUserProfilePicture, mUserMailAddress); // Create User on DB
//
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this,getString(R.string.error_authentication_canceled), Toast.LENGTH_LONG).show() ;
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,getString(R.string.error_no_internet), Toast.LENGTH_LONG).show() ;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this,getString(R.string.unknown_error), Toast.LENGTH_LONG).show() ;
                } else {
                    Toast.makeText(this,getString(R.string.unknown_error), Toast.LENGTH_LONG).show() ;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void getUserInfoFromFirebaseAuth() {
        mUserName = Objects.requireNonNull(getCurrentUser()).getDisplayName();
        mUserMailAddress = getCurrentUser().getEmail();
        mUserProfilePicture = Objects.requireNonNull(getCurrentUser().getPhotoUrl()).toString();
    }
}
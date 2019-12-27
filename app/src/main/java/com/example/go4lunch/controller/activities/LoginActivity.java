package com.example.go4lunch.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.Objects;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    // Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;
    private String mUserName, mUserMailAddress, mUserProfilePicture;

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this); // allow butterKnife on activity

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.TwitterBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setLogo(R.drawable.ic_logo_go4lunch_white)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }

    Context mContext = this;

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                getUserInfoFromFirebaseAuth(); // Get user info from firebase auth

                WorkmateHelper.isWorkmateExist(task -> {
                    if (task.isSuccessful()) { // they are document to parse
                        for (QueryDocumentSnapshot document : task.getResult())
                            if (document.getId().equals(mUserMailAddress)) {  // Is the document exist we leave
                                startActivity(new Intent(mContext, MainActivity.class)); // Start main activity if log succeed
                                return;
                            }
                        WorkmateHelper.createWorkmate(mUserName, mUserProfilePicture, mUserMailAddress); // Create User on DB
                    } else
                        Log.d("tag", "Error getting documents: ", task.getException());

                    startActivity(new Intent(mContext, MainActivity.class));
                });

            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this,getString(R.string.error_authentication_canceled), Toast.LENGTH_LONG).show() ;
                    logOut();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,getString(R.string.error_no_internet), Toast.LENGTH_LONG).show() ;
                } else {
                    Toast.makeText(this,getString(R.string.unknown_error), Toast.LENGTH_LONG).show() ;
                }
            }
        }
    }

    public void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> {
                    startActivity(new Intent(getApplicationContext(), StartActivity.class)); // Start LoginActivity after logOut
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void getUserInfoFromFirebaseAuth() {
        if ( getCurrentUser() == null)
        mUserName = "";
        else mUserName = getCurrentUser().getDisplayName();
        mUserMailAddress = getCurrentUser().getEmail();
        if (getCurrentUser().getPhotoUrl() == null)
            mUserProfilePicture = "";
        else mUserProfilePicture = getCurrentUser().getPhotoUrl().toString();
    }
}
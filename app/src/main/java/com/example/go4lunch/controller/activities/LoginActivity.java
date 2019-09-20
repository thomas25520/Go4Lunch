package com.example.go4lunch.controller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this); // allow butterKnife on activity
    }

    @OnClick(R.id.activity_login_connect_btn_mail)
    public void onClickMailButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityWithMail();
    }

    @OnClick(R.id.activity_login_connect_btn_facebook)
    public void onClickFacebookButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityWithFacebook();
    }

    @OnClick(R.id.activity_login_connect_btn_google)
    public void onClickGoogleButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityWithGoogle();
    }

    @OnClick(R.id.activity_login_connect_btn_twitter)
    public void onClickTweeterButton() {
        // 3 - Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityWithTwitter();
    }

    private void startSignInActivityWithMail() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build())) // Sign in with mail
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityWithFacebook() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build()))// Sign in with facebook
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityWithGoogle() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build())) // Sign in with google
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityWithTwitter() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.TwitterBuilder().build())) // Sign in with mail
                        .setIsSmartLockEnabled(false, true)
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
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this,getString(R.string.error_authentication_canceled), Toast.LENGTH_LONG).show() ;
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this,getString(R.string.error_no_internet), Toast.LENGTH_LONG).show() ;
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this,getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show() ;
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
}
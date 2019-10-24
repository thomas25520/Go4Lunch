package com.example.go4lunch.controller.data;

import com.google.android.libraries.places.api.model.PhotoMetadata;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class Restaurant {

    private String mName, mAddress, mDistance, mUserVote, mHours;
    private boolean  mOpening;
    private double mUserRating;
    private PhotoMetadata mPhotoMetadata;

    // GETTER
    public String getName() {
        return mName;
    }
    public String getAddress() {
        return mAddress;
    }
    public boolean getOpening() {
        return mOpening;
    }
    public String getDistance() {
        return mDistance;
    }
    public String getUserVote() {
        return mUserVote;
    }
    public String getHours() {
        return mHours;
    }
    public double getUserRating() {
        return mUserRating;
    }
    public PhotoMetadata getPhotoMetadata() {
        return mPhotoMetadata;
    }

    // SETTER
    public void setName(String name) {
        mName = name;
    }
    public void setAddress(String address) {
        mAddress = address;
    }
    public void setOpening(boolean opening) {
        mOpening = opening;
    }
    public void setDistance(String distance) {
        mDistance = distance;
    }
    public void setUserVote(String userVote) {
        mUserVote = userVote;
    }
    public void setHours(String hours) {
        mHours = hours;
    }
    public void setUserRating(double userRating) {
        mUserRating = userRating;
    }

    public void setPhotoMetadata(PhotoMetadata photoMetadata) {
        mPhotoMetadata = photoMetadata;
    }

    // CONSTRUCTOR
    public Restaurant(String name, String address, boolean opening, String hours, String distance, String userVote, Double rating, PhotoMetadata photoMetadata) {
        mName = name;
        mAddress = address;
        mOpening = opening;
        mHours = hours;
        mDistance = distance+ " m";
        mUserVote = "(" +userVote+ ")";
        mUserRating = rating;
        mPhotoMetadata = photoMetadata;
    }
}

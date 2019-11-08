package com.example.go4lunch.controller.data;

import com.google.android.libraries.places.api.model.PhotoMetadata;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class Restaurant {
    private String mName, mAddress, mDistance, mUserVote, mHours, mWebsiteUrl, mPhoneNumber, mId;
    private boolean mOpening;
    private double mUserRating;
    private PhotoMetadata mPhotoMetadata;

    // GETTER
    public String getName() {
        return mName;
    }
    public String getAddress() {
        return mAddress;
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
    public String getWebsiteUrl() {
        return mWebsiteUrl;
    }
    public String getPhoneNumber() {
        return mPhoneNumber;
    }
    public boolean isOpening() {
        return mOpening;
    }
    public double getUserRating() {
        return mUserRating;
    }
    public PhotoMetadata getPhotoMetadata() {
        return mPhotoMetadata;
    }
    public String getId() {
        return mId;
    }

    // SETTER
    public void setName(String name) {
        mName = name;
    }
    public void setAddress(String address) {
        mAddress = address;
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
    public void setWebsiteUrl(String websiteUrl) {
        mWebsiteUrl = websiteUrl;
    }
    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
    public void setOpening(boolean opening) {
        mOpening = opening;
    }
    public void setUserRating(double userRating) {
        mUserRating = userRating;
    }
    public void setPhotoMetadata(PhotoMetadata photoMetadata) {
        mPhotoMetadata = photoMetadata;
    }
    public void setId(String id) {
        mId = id;
    }

    public Restaurant(String name, String address, String distance, String userVote, String hours, String websiteUrl, String phoneNumber, String id, boolean opening, double userRating, PhotoMetadata photoMetadata) {
        mName = name;
        mAddress = address;
        mDistance = distance;
        mUserVote = userVote;
        mHours = hours;
        mWebsiteUrl = websiteUrl;
        mPhoneNumber = phoneNumber;
        mId = id;
        mOpening = opening;
        mUserRating = userRating;
        mPhotoMetadata = photoMetadata;
    }
}
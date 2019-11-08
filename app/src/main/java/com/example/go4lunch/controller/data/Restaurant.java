package com.example.go4lunch.controller.data;

import android.widget.ImageView;

import com.google.android.libraries.places.api.model.PhotoMetadata;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class Restaurant {

    private String mName, mAddress, mDistance, mUserVote, mHours;
    private ImageView mStar1, mStar2, mStar3; // FIXME: 11/09/2019 Stars is gone while user no vote + algo todo
    private boolean  mOpening;
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
    public ImageView getStar1() {
        return mStar1;
    }
    public ImageView getStar2() {
        return mStar2;
    }
    public ImageView getStar3() {
        return mStar3;
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
    public void setStar1(ImageView star1) {
        mStar1 = star1;
    }
    public void setStar2(ImageView star2) {
        mStar2 = star2;
    }
    public void setStar3(ImageView star3) {
        mStar3 = star3;
    }
    public void setPhotoMetadata(PhotoMetadata photoMetadata) {
        mPhotoMetadata = photoMetadata;
    }

    // CONSTRUCTOR
    public Restaurant (String name, String address, boolean opening, String hours, String distance, String userVote, PhotoMetadata photoMetadata) {
        mName = name;
        mAddress = address;
        mOpening = opening;
        mHours = hours;
        mDistance = distance+ " m";
        mUserVote = "(" +userVote+ ")";
        mPhotoMetadata = photoMetadata;
    }
}

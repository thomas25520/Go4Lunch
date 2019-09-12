package com.example.go4lunch.controller.data;

import android.widget.ImageView;

/**
 * Created by Dutru Thomas on 10/09/2019.
 */
public class Restaurant {

    private String mName, mOrigin, mAddress, mOpening, mDistance, mUserVote, mHours, mPictureUrl;
    private ImageView mStar1, mStar2, mStar3, mPicture; // FIXME: 11/09/2019 Stars is gone while user no vote + algo todo

    // GETTER
    public String getName() {
        return mName;
    }
    public String getOrigin() {
        return mOrigin;
    }
    public String getAddress() {
        return mAddress;
    }
    public String getOpening() {
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
    public String getPictureUrl() {
        return mPictureUrl;
    }
    public ImageView getPicture() {
        return mPicture;
    }

    // SETTER
    public void setName(String name) {
        mName = name;
    }
    public void setOrigin(String origin) {
        mOrigin = origin;
    }
    public void setAddress(String address) {
        mAddress = address;
    }
    public void setOpening(String opening) {
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
    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }
    public void setPicture(ImageView picture) {
        mPicture = picture;
    }

    // CONSTRUCTOR
    public Restaurant (String name, String origin, String address, String opening, String hours, String distance, String userVote, String pictureUrl) {
        mName = name;
        mOrigin = origin;
        mAddress = address;
        mOpening = opening;
        mHours = hours;
        mDistance = distance+ " m";
        mUserVote = "(" +userVote+ ")";
        mPictureUrl = pictureUrl;
    }
}

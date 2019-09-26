package com.example.go4lunch.model;

/**
 * Created by Dutru Thomas on 24/09/2019.
 */
public class Restaurant {
    private String mRestaurantName, mOrigin, mPictureUrl;
    private boolean mOpenOrClose;
    private int mOpeningHours, mDistanceFrom, mNumbersOfWorkmatesEats, mVote;

// --- CONSTRUCTORS ---
    public Restaurant(){}
    public Restaurant(String restaurantName, String origin, String pictureUrl, boolean openOrClose, int openingHours, int distanceFrom, int numbersOfWorkmatesEats, int vote) {
        mRestaurantName = restaurantName;
        mOrigin = origin;
        mPictureUrl = pictureUrl;
        mOpenOrClose = openOrClose;
        mOpeningHours = openingHours;
        mDistanceFrom = distanceFrom;
        mNumbersOfWorkmatesEats = numbersOfWorkmatesEats;
        mVote = vote;
    }

    // --- GETTERS ---
    public String getRestaurantName() {
        return mRestaurantName;
    }
    public String getOrigin() {
        return mOrigin;
    }
    public String getPictureUrl() {
        return mPictureUrl;
    }
    public boolean isOpenOrClose() {
        return mOpenOrClose;
    }
    public int getOpeningHours() {
        return mOpeningHours;
    }
    public int getDistanceFrom() {
        return mDistanceFrom;
    }
    public int getNumbersOfWorkmatesEats() {
        return mNumbersOfWorkmatesEats;
    }
    public int getVote() {
        return mVote;
    }

    // --- SETTERS ---
    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }
    public void setOrigin(String origin) {
        mOrigin = origin;
    }
    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }
    public void setOpenOrClose(boolean openOrClose) {
        mOpenOrClose = openOrClose;
    }
    public void setOpeningHours(int openingHours) {
        mOpeningHours = openingHours;
    }
    public void setDistanceFrom(int distanceFrom) {
        mDistanceFrom = distanceFrom;
    }
    public void setNumbersOfWorkmatesEats(int numbersOfWorkmatesEats) {
        mNumbersOfWorkmatesEats = numbersOfWorkmatesEats;
    }
    public void setVote(int vote) {
        mVote = vote;
    }
}

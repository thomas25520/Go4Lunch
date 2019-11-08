package com.example.go4lunch.controller.data;

/**
 * Created by Dutru Thomas on 13/09/2019.
 */
public class Workmates {
    private String mUserFirstName, mUserIsEating, mRestaurantOrigin, mRestaurantName, mUserPictureUrl;

    // GETTER
    public String getUserFirstName() {
        return mUserFirstName;
    }
    public String getUserIsEating() {
        if(mUserIsEating.equals("false")) {// No display "false" if user eat anywhere
            setUserIsEating("");
        } else {
            setUserIsEating("- is eating"); // Display "is eating" if user eat on restaurant
        }
        return mUserIsEating;
    }
    public String getRestaurantOrigin() {
        return mRestaurantOrigin;
    }
    public String getRestaurantName() {
        return mRestaurantName;
    }
    public String getUserPictureUrl() {
        return mUserPictureUrl;
    }

    // SETTER
    public void setUserFirstName(String userFirstName) {
        mUserFirstName = userFirstName;
    }
    public void setUserIsEating(String userIsEating) {
        mUserIsEating = userIsEating;
    }
    public void setRestaurantOrigin(String restaurantOrigin) {
        mRestaurantOrigin = restaurantOrigin;
    }
    public void setRestaurantName(String restaurantName) {
        mRestaurantName = restaurantName;
    }
    public void setUserPictureUrl(String userPictureUrl) {
        mUserPictureUrl = userPictureUrl;
    }

    // CONSTRUCTOR
    public Workmates(String userFirstName, String userIsEating, String restaurantOrigin, String restaurantName, String userPictureUrl) {
        mUserFirstName = userFirstName;
        mUserIsEating = userIsEating;
        mRestaurantOrigin = restaurantOrigin;
        mRestaurantName = restaurantName;
        mUserPictureUrl = userPictureUrl;
    }
}

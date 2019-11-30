package com.example.go4lunch.data;

/**
 * Created by Dutru Thomas on 13/09/2019.
 */
public class Workmate {
    private String mName, mRestaurantId, mPictureUrl, mEmail;
    private boolean mEating;

    // GETTER
    public String getName() {
        return mName;
    }
    public String getRestaurantId() {
        return mRestaurantId;
    }
    public String getPictureUrl() {
        return mPictureUrl;
    }
    public String getEmail() {
        return mEmail;
    }
    public boolean isEating() {
        return mEating;
    }

    // SETTER
    public void setName(String name) {
        mName = name;
    }
    public void setRestaurantId(String restaurantId) {
        mRestaurantId = restaurantId;
    }
    public void setPictureUrl(String pictureUrl) {
        mPictureUrl = pictureUrl;
    }
    public void setEmail(String email) {
        mEmail = email;
    }
    public void setEating(boolean eating) {
        mEating = eating;
    }

    // CONSTRUCTOR
    public Workmate(String name, String restaurantId, String pictureUrl, String email, boolean eating) {
        mName = name;
        mRestaurantId = restaurantId;
        mPictureUrl = pictureUrl;
        mEmail = email;
        mEating = eating;
    }
}

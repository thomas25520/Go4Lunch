package com.example.go4lunch.model;

/**
 * Created by Dutru Thomas on 24/09/2019.
 */
public class User {
    private String mUserName, mUserUrlProfilePicture, mUserEmail;
    private Boolean mIsEating;

    // --- CONSTRUCTORS ---

    public User(){}
    public User(String userName, String userUrlProfilePicture, String userEmail, Boolean isEating) {
        mUserName = userName;
        mUserUrlProfilePicture = userUrlProfilePicture;
        mUserEmail = userEmail;
        mIsEating = isEating;
    }

    // --- GETTERS ---
    public String getUserName() {
        return mUserName;
    }
    public String getUserUrlProfilePicture() {
        return mUserUrlProfilePicture;
    }
    public String getUserEmail() {
        return mUserEmail;
    }
    public Boolean getEating() {
        return mIsEating;
    }


    // --- SETTERS ---
    public void setUserName(String userName) {
        mUserName = userName;
    }
    public void setUserUrlProfilePicture(String userUrlProfilePicture) {
        mUserUrlProfilePicture = userUrlProfilePicture;
    }
    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }
    public void setEating(Boolean eating) {
        mIsEating = eating;
    }
}

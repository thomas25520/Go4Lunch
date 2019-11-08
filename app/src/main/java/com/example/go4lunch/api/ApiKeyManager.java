package com.example.go4lunch.api;

/**
 * Created by Dutru Thomas on 23/10/2019.
 */
public class ApiKeyManager {

    private final String googleMapsApiKey = "AIzaSyA5AiL719efOapJiyfgf-RSBq7BN6FWDM4";
    private final String facebookApplicationId = "3019484981460119";
    private final String facebookLoginProtocolSecret = "fb3019484981460119";
    private final String twitterConsumerKey = "3sI4Kl5kaQUagM1YfUU0gpKzU";
    private final String twitterConsumerSecret = "Yse7FKJOgqBouYhWsx0EErSNbkF71vyhSh15LMVbFKv3EpoNBJ";

    public String getFacebookApplicationId() {
        return facebookApplicationId;
    }

    public String getFacebookLoginProtocolSecret() {
        return facebookLoginProtocolSecret;
    }

    public String getTwitterConsumerKey() {
        return twitterConsumerKey;
    }

    public String getTwitterConsumerSecret() {
        return twitterConsumerSecret;
    }

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}

package com.example.go4lunch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
    private static SharedPreferences mPreferences;

    // Save int in sharedPreferences
    public static void putInt(Context context, String key, int value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.edit().putInt(key, value).apply();
    }

    // Get int from sharedPreferences
    public static int getInt(Context context, String key) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreferences.getInt(key, -1);
    }

    // Save boolean in sharedPreferences
    public static void putBoolean(Context context, String key, boolean value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.edit().putBoolean(key, value).apply();
    }

    // Get boolean from sharedPreferences
    public static boolean getBoolean(Context context, String key) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreferences.getBoolean(key,false);
    }

    // Save string in sharedPreferences
    public static void putString(Context context, String key, String value) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences.edit().putString(key, value).apply();
    }

    // Get string from sharedPreferences
    public static String getString(Context context, String key) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreferences.getString(key,"");
    }
}
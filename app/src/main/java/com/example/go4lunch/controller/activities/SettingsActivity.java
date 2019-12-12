package com.example.go4lunch.controller.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.utils.SharedPreferencesManager;

public class SettingsActivity extends AppCompatActivity {
    TimePicker mPicker;
    Switch mBtnNotification;
    int mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
    }

    private void initViews() {
        mPicker = findViewById(R.id.datePickerNotification);
        mBtnNotification = findViewById(R.id.notification_btn);

        mPicker.setIs24HourView(true);

        // Set notification btn state
        if (SharedPreferencesManager.getBoolean(this, "notificationBtnState")) {
            mBtnNotification.setChecked(true);
        } else {
            mBtnNotification.setChecked(false);
        }

        // Set datePicker
        if(SharedPreferencesManager.getInt(this, "mHour") != -1 && SharedPreferencesManager.getInt(this, "mMinute") != -1 ) {
            if (Build.VERSION.SDK_INT >= 23) {
                mPicker.setHour(SharedPreferencesManager.getInt(this, "mHour"));
                mPicker.setMinute(SharedPreferencesManager.getInt(this, "mMinute"));
            } else {
            mPicker.setCurrentHour(SharedPreferencesManager.getInt(this, "mHour"));
            mPicker.setCurrentMinute(SharedPreferencesManager.getInt(this, "mMinute"));
            }
        }

        // Set action notification btn
        mBtnNotification.setOnClickListener(view -> {

            if (mBtnNotification.isChecked()) {
                if (Build.VERSION.SDK_INT >= 23) {
                    mHour = mPicker.getHour();
                    mMinute = mPicker.getMinute();

                } else {
                    mHour = mPicker.getCurrentHour();
                    mMinute = mPicker.getCurrentMinute();
                }

                SharedPreferencesManager.putInt(this, "mHour", mHour);
                SharedPreferencesManager.putInt(this, "mMinute", mMinute);
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", true);
                // TODO: 12/12/2019 Notification ON 
            } else {
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", false);

                // TODO: 12/12/2019 Notification OFF
            }

        });

        // Save hours without unchecked notification btn
        mPicker.setOnTimeChangedListener((timePicker, i, i1) -> {
            if (Build.VERSION.SDK_INT >= 23) {
                mHour = mPicker.getHour();
                mMinute = mPicker.getMinute();

            } else {
                mHour = mPicker.getCurrentHour();
                mMinute = mPicker.getCurrentMinute();
            }

            SharedPreferencesManager.putInt(this, "mHour", mHour);
            SharedPreferencesManager.putInt(this, "mMinute", mMinute);
        });
    }
}
package com.example.go4lunch.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.utils.AlarmReceiver;
import com.example.go4lunch.utils.SharedPreferencesManager;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    TimePicker mPicker;
    Switch mBtnNotification;
    int mHour, mMinute;
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;
    Intent mNotificationIntent;

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

                SharedPreferencesManager.putBoolean(this, "notificationBtnState", true); // Save btn notification state
                scheduleNotification(mHour, mMinute, AlarmManager.INTERVAL_DAY); // Send info for notification if button is checked and enable alarm

            } else {
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", false);
                scheduleNotification(mHour, mMinute, AlarmManager.INTERVAL_DAY); // FIXME: 16/12/2019 recreate schedule for nothing prevent apk crash if uncheck notification btn
                mAlarmManager.cancel(mPendingIntent); // Disable alarm
            }
        });

        // Save hours when picker changed
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

            if (SharedPreferencesManager.getBoolean(this, "notificationBtnState"))
            scheduleNotification(mHour, mMinute, AlarmManager.INTERVAL_DAY); // Send info for notification if button is checked and enable alarm
        });
    }

    private void scheduleNotification(int hour, int minute, long intervalDay) {
        mNotificationIntent = new Intent(this, AlarmReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalDay, mPendingIntent);
    }
}
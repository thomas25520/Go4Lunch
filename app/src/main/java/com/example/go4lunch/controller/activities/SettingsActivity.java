package com.example.go4lunch.controller.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.utils.AlarmReceiver;
import com.example.go4lunch.utils.SharedPreferencesManager;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {
    Switch mBtnNotification;
    int mHour = 12, mMinute = 0;
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
        mBtnNotification = findViewById(R.id.notification_btn);

        // Set notification btn state
        if (SharedPreferencesManager.getBoolean(this, "notificationBtnState")) {
            mBtnNotification.setChecked(true);
        } else {
            mBtnNotification.setChecked(false);
        }

        // Set action notification btn
        mBtnNotification.setOnClickListener(view -> {
            if (mBtnNotification.isChecked()) {
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", true); // Save btn notification state
                scheduleNotification(mHour, mMinute, AlarmManager.INTERVAL_DAY); // Send info for notification if button is checked and enable alarm

            } else {
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", false);
                scheduleNotification(mHour, mMinute, AlarmManager.INTERVAL_DAY); // Recreate schedule, prevent apk crash if notification btn is unchecked.
                mAlarmManager.cancel(mPendingIntent); // Disable alarm
            }
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
package com.example.go4lunch.controller.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.example.go4lunch.utils.SharedPreferencesManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class SettingsActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "notification";
    TimePicker mPicker;
    Switch mBtnNotification;
    int mHour, mMinute;

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }


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
                enableNotification(true);
            } else {
                SharedPreferencesManager.putBoolean(this, "notificationBtnState", false);

                // TODO: 12/12/2019 Notification OFF
                enableNotification(false);
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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notifications);
            String description = getString(R.string.notifications);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void enableNotification(boolean bool) {
        if (bool) { // Enable notification
            createNotificationChannel();

            Task<DocumentSnapshot> taskSnapshot  = WorkmateHelper.getWorkmate(getCurrentUser().getEmail());
            taskSnapshot.addOnCompleteListener(task -> { // access to DB
                if (task.isSuccessful()) {
                    String restaurantName = WorkmateHelper.getStringInfoFrom("restaurantName", taskSnapshot.getResult());

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID) // Notification content
                            .setSmallIcon(R.drawable.ic_logo_go4lunch)
                            .setContentTitle("Your Lunch :")
                            .setContentText(getString(R.string.you_eating_at) + restaurantName)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                    notificationManager.notify(1, builder.build()); // Display notification
                }
            });

        } else { // Disable notification
            // TODO: 12/12/2019 notification disable
        }
    }
}
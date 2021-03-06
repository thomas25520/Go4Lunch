package com.example.go4lunch.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunch.Constant;
import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "chanel_1";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_NAME = "chanel_1";

    Context mContext;
    String mRestaurantName;

    SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");
    Calendar calendar = Calendar.getInstance();
    String mCurrentDay = mDayFormat.format(calendar.getTime());
    String mDayFromSharedPref;

    String mUsersEating = "";

    String content = ""; // This string contain all info to display on notification

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mDayFromSharedPref = SharedPreferencesManager.getString(mContext, "dayFromSharedPref");  // get day from shared pref

        if (!mCurrentDay.equals(mDayFromSharedPref)) { // compare current day with day in sharedPref, if is different, the user is notify
            Task<DocumentSnapshot> taskSnapshot  = WorkmateHelper.getWorkmate(getCurrentUser().getEmail());
            taskSnapshot.addOnCompleteListener(task -> { // access to DB
                if (task.isSuccessful()) {
                    mRestaurantName = WorkmateHelper.getStringInfoFrom(Constant.RESTAURANT_NAME, taskSnapshot.getResult());
                    String mRestaurantId = WorkmateHelper.getStringInfoFrom(Constant.RESTAURANT_ID, taskSnapshot.getResult());

                    WorkmateHelper.getWorkmatesCollection().get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task1.getResult())) { // Get all users
                                        if(Objects.equals(document.getString(Constant.RESTAURANT_ID), mRestaurantId)) {
                                            if(!getCurrentUser().getDisplayName().equals(document.getString("name"))) // Not add current user on notification
                                            mUsersEating += Objects.requireNonNull(document.get("name")).toString() + ", "; // Add user and print "," after
                                        }
                                    }
                                    if (!mRestaurantName.isEmpty()) {
                                        if (!mUsersEating.isEmpty()) { // If one or more users eat with currentUser display restaurant name + users
                                            content = mRestaurantName + " " + context.getString(R.string.with) + " " + mUsersEating;
                                            createAndShowNotification(R.drawable.ic_logo_go4lunch, mContext.getString(R.string.you_eating_at), content, NotificationCompat.PRIORITY_DEFAULT, CHANNEL_ID, NOTIFICATION_ID, mContext); // notify user
                                        } else { // If anyone eat with currentUser, display only restaurant
                                            createAndShowNotification(R.drawable.ic_logo_go4lunch, mContext.getString(R.string.you_eating_at), mRestaurantName, NotificationCompat.PRIORITY_DEFAULT, CHANNEL_ID, NOTIFICATION_ID, mContext); // notify user
                                        }
                                        mDayFromSharedPref = mCurrentDay; // replace day in sharedPref by current day, this is to avoid to display again notification, the user is only notify once per day
                                        SharedPreferencesManager.putString(mContext, "dayFromSharedPref", mDayFromSharedPref); // save on sharedPref
                                    }
                                }
                            });
                }
            });
        }
    }

    // Step 1 : Create a channel with importance by default.
    private void createNotificationChannel(Context context, String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, CHANNEL_NAME, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Step 2 : create the notification.
    public NotificationCompat.Builder createNotification(int icon, String title, String content, int notificationCompat, String channelId, Context context) {
        return new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setPriority(notificationCompat);
    }

    // Step 3 : Show the notification.
    public void showNotification(int notificationId, Context context, NotificationCompat.Builder notificationBuilder) {
        NotificationManagerCompat.from(context).notify(notificationId, notificationBuilder.build());
    }

    public void createAndShowNotification(int icon, String title, String content, int notificationCompat, String channelId, int notificationId, Context context) {
        createNotificationChannel(context, channelId);
        NotificationCompat.Builder notificationBuilder = createNotification(icon, title, content, notificationCompat, channelId, context);
        showNotification(notificationId, context, notificationBuilder);
    }
}
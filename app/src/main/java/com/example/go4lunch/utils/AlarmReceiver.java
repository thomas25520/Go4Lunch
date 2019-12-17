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

import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkmateHelper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "chanel_1";
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_NAME = "chanel_1";

    Context mContext;
    String mRestaurantName;

    @Nullable
    protected FirebaseUser getCurrentUser() { return FirebaseAuth.getInstance().getCurrentUser(); }

    public void onReceive(Context context, Intent intent) {
        mContext = context;

        Task<DocumentSnapshot> taskSnapshot  = WorkmateHelper.getWorkmate(getCurrentUser().getEmail());
        taskSnapshot.addOnCompleteListener(task -> { // access to DB
            if (task.isSuccessful()) {
                mRestaurantName = WorkmateHelper.getStringInfoFrom("restaurantName", taskSnapshot.getResult());

                if (!mRestaurantName.isEmpty())
                    createAndShowNotification(R.drawable.ic_logo_go4lunch, mContext.getString(R.string.you_eating_at), mRestaurantName, NotificationCompat.PRIORITY_DEFAULT, CHANNEL_ID, NOTIFICATION_ID, mContext); // notify user
            }
        });
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
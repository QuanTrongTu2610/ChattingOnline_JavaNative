package com.example.chattingonlineapplication.BroadCast;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.InstanceProvider;
import com.example.chattingonlineapplication.R;

public class UserParticipantReceiver extends BroadcastReceiver {

    private static final String TAG = UserParticipantReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "com.example.chattingonlineapplication.groupchat.participant";
    private static final String textTitle = "Title";
    private static final String textContent = "Content";
    private User user;
    private String action;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            user = (User) intent.getSerializableExtra("USER");
            action = (String) intent.getStringExtra("ACTION");
            // make notification
            String titlePrefict = action.equalsIgnoreCase(InstanceProvider.ADD) ? "User get in: " : "User get out: ";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_outline_notifications_24)
                    .setContentTitle(titlePrefict + user.getUserFirstName() + " " + user.getUserLastName())
                    .setContentText("IP: " + user.getUserIpAddress() + " Port: " + user.getUserPort())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            Log.i(TAG, "Alert Notification");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(111, builder.build());
        }
    }
}

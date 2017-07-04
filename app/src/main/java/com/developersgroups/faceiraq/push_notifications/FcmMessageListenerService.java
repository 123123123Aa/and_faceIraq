package com.developersgroups.faceiraq.push_notifications;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.developersgroups.faceiraq.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Paweł Sałata on 27.06.2017.
 * email: psalata9@gmail.com
 */

public class FcmMessageListenerService extends FirebaseMessagingService {

    public static final String TAG = "FcmListener";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Log.d(TAG, "onMessageReceived, from: " + from);
        Log.d(TAG, "onMessageReceived, notificationBody: " + remoteMessage.getNotification().getBody());

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody());
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification.build());
//        Toast.makeText(this, "Push notification received, please contact our developer for more information", Toast.LENGTH_LONG).show();
    }
}

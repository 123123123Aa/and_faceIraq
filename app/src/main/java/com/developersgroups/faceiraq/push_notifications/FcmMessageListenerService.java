package com.developersgroups.faceiraq.push_notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.developersgroups.faceiraq.MainActivity;
import com.developersgroups.faceiraq.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

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



        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, buildNotification(remoteMessage));
//        Toast.makeText(this, "Push notification received, please contact our developer for more information", Toast.LENGTH_LONG).show();
    }

    private Notification buildNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        String url = data.get("url");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity (
                this,
                new Random().nextInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setOngoing(false)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return notification.build();
    }
}

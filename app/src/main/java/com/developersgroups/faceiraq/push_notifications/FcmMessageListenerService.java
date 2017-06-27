package com.developersgroups.faceiraq.push_notifications;

import android.app.NotificationManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
//        Toast.makeText(this, "Push notification received, please contact our developer for more information", Toast.LENGTH_LONG).show();
    }
}

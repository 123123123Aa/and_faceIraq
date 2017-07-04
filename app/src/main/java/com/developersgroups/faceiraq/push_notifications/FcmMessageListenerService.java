package com.developersgroups.faceiraq.push_notifications;

import android.util.Log;

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
//        Toast.makeText(this, "Push notification received, please contact our developer for more information", Toast.LENGTH_LONG).show();
    }
}

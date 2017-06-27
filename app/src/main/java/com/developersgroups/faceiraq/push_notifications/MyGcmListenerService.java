package com.developersgroups.faceiraq.push_notifications;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Paweł Sałata on 05.05.2017.
 * email: psalata9@gmail.com
 */

public class MyGcmListenerService extends GcmListenerService {

    public static final String TAG = "GcmListener";

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
        Log.d(TAG, "onMessageReceived: ");
        try {

        Toast.makeText(this, "Received push notification", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

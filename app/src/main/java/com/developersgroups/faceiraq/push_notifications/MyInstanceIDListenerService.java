package com.developersgroups.faceiraq.push_notifications;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Paweł Sałata on 05.05.2017.
 * email: psalata9@gmail.com
 */

public class MyInstanceIDListenerService extends InstanceIDListenerService {

    public static final String TAG = "InstanceIdListener";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh: ");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}

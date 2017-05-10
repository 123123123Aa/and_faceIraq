package net.faceiraq.and_faceiraq.push_notifications;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

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
    }
}

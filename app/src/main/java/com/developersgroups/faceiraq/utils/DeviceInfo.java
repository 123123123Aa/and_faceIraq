package com.developersgroups.faceiraq.utils;

import android.os.Build;

/**
 * Created by Paweł Sałata on 26.05.2017.
 * email: psalata9@gmail.com
 */

public class DeviceInfo {

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getPlatform() {
        return "Android";
    }

    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }
}

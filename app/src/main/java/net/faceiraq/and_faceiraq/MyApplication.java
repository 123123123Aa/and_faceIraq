package net.faceiraq.and_faceiraq;

import android.app.Application;

import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;

import java.util.UUID;

/**
 * Created by Paweł Sałata on 17.05.2017.
 * email: psalata9@gmail.com
 */

public class MyApplication extends Application {

    private boolean firstTimeOpened = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (SharedPreferencesHelper.getUUID(this).isEmpty()) {
            String uuid = UUID.randomUUID().toString();
            SharedPreferencesHelper.setUUID(this, uuid);
        }
    }
    public boolean isOpen() {
        return  firstTimeOpened;
    }

    public void setOpen() {
        firstTimeOpened = false;
    }
}

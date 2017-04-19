package com.ready4s.faceiraq.and_faceiraq.model.database;

import android.graphics.Bitmap;
import android.util.Base64;

import com.ready4s.faceiraq.and_faceiraq.model.database.history.HistoryRecord;
import com.ready4s.faceiraq.and_faceiraq.model.utils.ImageUtil;

import io.realm.RealmObject;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class PageDetails {

    private String title;
    private String address;
    private Bitmap logo;

    public PageDetails() {
    }

    public PageDetails(String title, String address, Bitmap logo) {
        this.title = title;
        this.address = address;
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public String getBase64Logo() {
        return ImageUtil.encodeToBase64(logo, Bitmap.CompressFormat.PNG, 100);
    }

    public void setLogo(Bitmap logo) {
        this.logo = logo;
    }


    @Override
    public String toString() {
        return "[" + title + "] " + address;
    }
}

package net.faceiraq.and_faceiraq.model;

import android.graphics.Bitmap;

import net.faceiraq.and_faceiraq.model.utils.ImageUtil;

import java.util.UUID;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class PageDetails {

    private String uuid;
    private long timestamp;
    private String title;
    private String address;
    private Bitmap logo;

    public PageDetails() {
        uuid = UUID.randomUUID().toString();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void clear() {
        title = "";
        address = "";
        logo = null;
    }
}

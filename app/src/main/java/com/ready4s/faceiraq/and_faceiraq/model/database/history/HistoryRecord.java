package com.ready4s.faceiraq.and_faceiraq.model.database.history;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class HistoryRecord extends RealmObject {

    @PrimaryKey
    private long timestamp;
    private String title;
    private String address;
    private String base64Logo;

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

    public String getBase64Logo() {
        return base64Logo;
    }

    public void setBase64Logo(String base64Logo) {
        this.base64Logo = base64Logo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return timestamp + ": [" + title + "] " + address + ", logo: " + base64Logo;
    }
}

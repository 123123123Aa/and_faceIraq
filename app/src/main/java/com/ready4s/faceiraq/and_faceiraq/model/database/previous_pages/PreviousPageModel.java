package com.ready4s.faceiraq.and_faceiraq.model.database.previous_pages;

import io.realm.RealmObject;

/**
 * Created by Paweł Sałata on 26.04.2017.
 * email: psalata9@gmail.com
 */

public class PreviousPageModel extends RealmObject {

    public static final String URL = "url";

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

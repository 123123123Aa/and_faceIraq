package net.faceiraq.and_faceiraq.model.database.opened_pages;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Paweł Sałata on 23.04.2017.
 * email: psalata9@gmail.com
 */

public class OpenedPageModel extends RealmObject {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String SCREENSHOT = "screenshot";

    @PrimaryKey
    private long id;
    private String title;
    private String url;
    private byte[] screenshot;

    public OpenedPageModel() {
        title = "";
        url = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(byte[] screenshot) {
        this.screenshot = screenshot;
    }
}

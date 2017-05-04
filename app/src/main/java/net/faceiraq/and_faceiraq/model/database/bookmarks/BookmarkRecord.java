package net.faceiraq.and_faceiraq.model.database.bookmarks;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Paweł Sałata on 20.04.2017.
 * email: psalata9@gmail.com
 */

public class BookmarkRecord extends RealmObject {


    public static final String TIMESTAMP = "timestamp";
    public static final String TITLE = "title";
    public static final String ADDRESS = "address";
    public static final String BASE64LOGO = "base64Logo";
    public static final String ID = "id";

    @PrimaryKey
    private long id;
    private long timestamp;
    private String title;
    private String address;
    private String base64Logo;

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
        return id + ": [" + title + "] " + address + ", logo: " + base64Logo;
    }
}

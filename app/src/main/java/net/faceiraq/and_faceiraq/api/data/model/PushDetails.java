package net.faceiraq.and_faceiraq.api.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 15.05.2017.
 */

public class PushDetails {
    @SerializedName("uuid")
    String uuid;
    @SerializedName("is_active")
    boolean isActive;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

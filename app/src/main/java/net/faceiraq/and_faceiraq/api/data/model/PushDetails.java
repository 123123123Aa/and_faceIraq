package net.faceiraq.and_faceiraq.api.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 15.05.2017.
 */

public class PushDetails {
    @SerializedName("is_active")
    boolean isActive;
    @SerializedName("uuid")
    String uuid;

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

    public boolean getActive() {
        return isActive;
    }
}

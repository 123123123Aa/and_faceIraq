package com.developersgroups.faceiraq.api.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Paweł Sałata on 27.06.2017.
 * email: psalata9@gmail.com
 */

public class EmailResponse {

    @SerializedName("status")
    public String status;
    @SerializedName("message")
    public String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

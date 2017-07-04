package com.developersgroups.faceiraq.api.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Paweł Sałata on 04.07.2017.
 * email: psalata9@gmail.com
 */

public class EmailModel {
    @SerializedName("subject")
    String subject;
    @SerializedName("message")
    String message;
    @SerializedName("email")
    String email;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subjectt) {
        this.subject = subjectt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

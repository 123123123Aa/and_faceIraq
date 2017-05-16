package net.faceiraq.and_faceiraq.api.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sebastian on 10.02.2017.
 */

public class ErrorBody {

    @SerializedName("message")
    public String mMessage;
    @SerializedName("code")
    public int mCode;

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getmCode() {
        return mCode;
    }

    public void setmCode(int mCode) {
        this.mCode = mCode;
    }

    @Override
    public String toString() {
        return "ErrorBody{" +
                "mMessage='" + mMessage + '\'' +
                ", mCode=" + mCode +
                '}';
    }
}

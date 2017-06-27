package com.developersgroups.faceiraq.api;

import com.developersgroups.faceiraq.api.data.model.PushDetails;
import com.developersgroups.faceiraq.api.data.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by user on 15.05.2017.
 */

public interface ApiCalls {

    @POST("app/api.php?action=pushSetting")
    Call<Void> allowPushService(@Body PushDetails pushDetails);

    @FormUrlEncoded
    @POST("app/api.php?action=regUser")
    Call<RegisterResponse> registerUser(
            @Field("regID") String deviceToken,
            @Field("uuid") String uuid,
            @Field("model") String model,
            @Field("platform") String platform,
            @Field("version") String version);
}

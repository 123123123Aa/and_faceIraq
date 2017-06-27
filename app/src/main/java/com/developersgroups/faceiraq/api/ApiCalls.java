package com.developersgroups.faceiraq.api;

import com.developersgroups.faceiraq.api.data.model.PushDetails;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by user on 15.05.2017.
 */

public interface ApiCalls {

    @POST("app/api.php?action=pushSetting")
    Call<Void> allowPushService(@Body PushDetails pushDetails);

    @POST("app/api.php?action=regUser")
    Call<Void> registerUser(@Query("regID") String deviceToken,
                            @Query("uuid") String uuid,
                            @Query("model") String model,
                            @Query("platform") String platform,
                            @Query("version") String version);
}

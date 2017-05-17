package net.faceiraq.and_faceiraq.api;

import net.faceiraq.and_faceiraq.api.data.model.PushDetails;
import net.faceiraq.and_faceiraq.api.data.response.PushResponse;
import net.faceiraq.and_faceiraq.api.data.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by user on 15.05.2017.
 */

public interface ApiCalls {
    @POST("api.php?action=pushSetting")
    Call<PushResponse> allowPushService(@Body PushDetails pushDetails);

    @POST("app/api.php?action=regUser")
    Call<Void> registerUser(@Query("regID") String deviceToken,
                                        @Query("uuid") String uuid,
                                        @Query("model") String model,
                                        @Query("platform") String platform,
                                        @Query("version") String version);
}

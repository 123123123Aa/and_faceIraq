package net.faceiraq.and_faceiraq.api;

import net.faceiraq.and_faceiraq.api.data.model.PushDetails;
import net.faceiraq.and_faceiraq.api.data.response.PushResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by user on 15.05.2017.
 */

public interface ApiCalls {
    @POST("api.php?action=pushSetting")
    Call<PushResponse> allowPushService(@Body PushDetails pushDetails);


}

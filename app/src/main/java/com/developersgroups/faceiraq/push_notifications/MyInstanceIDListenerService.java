package com.developersgroups.faceiraq.push_notifications;

import android.util.Log;

import com.developersgroups.faceiraq.api.ApiCalls;
import com.developersgroups.faceiraq.api.ApiManager;
import com.developersgroups.faceiraq.api.data.response.RegisterResponse;
import com.developersgroups.faceiraq.model.SharedPreferencesHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Paweł Sałata on 05.05.2017.
 * email: psalata9@gmail.com
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    public static final String TAG = "InstanceIdListener";
    private ApiCalls api;

    @Override
    public void onCreate() {
        super.onCreate();
        ApiManager.init(this);
        api = ApiManager.get().getApi();
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "onTokenRefresh, token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: token=" + token);
        Call<RegisterResponse> call = api.registerUser(
                token,
                SharedPreferencesHelper.getUUID(this)
                ,""
                ,""
                ,"");
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                Log.d(TAG, "onResponse, code: " + response.code());
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

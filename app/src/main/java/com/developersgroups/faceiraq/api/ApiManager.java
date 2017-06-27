package com.developersgroups.faceiraq.api;

import android.content.Context;

import com.developersgroups.faceiraq.api.data.response.ErrorBody;
import com.github.simonpercic.oklog3.OkLogInterceptor;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 15.05.2017.
 */

public class ApiManager {


    public static final String API_BASE_URL = "http://www.faceiraq.net/";

    private static ApiManager INSTANCE;
    private final Retrofit mRetrofit;
    private ApiCalls apiCalls;

    private ApiManager(Context appContext) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkLogInterceptor interceptor = OkLogInterceptor.builder()
                .build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(20, TimeUnit.SECONDS);
        httpClient.readTimeout(20, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(interceptor);
        httpClient.addNetworkInterceptor(logging);
        httpClient.followRedirects(true);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .validateEagerly(true)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        apiCalls = mRetrofit.create(ApiCalls.class);
    }

    public static void init(Context appContext) {
        INSTANCE = new ApiManager(appContext);
    }

    public static ApiManager get() {
        return INSTANCE;
    }

    public static ErrorBody parseError(ResponseBody response) {

        Converter<ResponseBody, ErrorBody> converter = INSTANCE.mRetrofit.responseBodyConverter(ErrorBody.class, new Annotation[0]);

        ErrorBody error;

        try {
            error = converter.convert(response);
        } catch (IOException e) {
            return new ErrorBody();
        }

        return error;
    }

    public static ApiErrorCodes parseApiError(Throwable t) {
        if (t instanceof UnknownHostException)
            return ApiErrorCodes.NO_INTERNET;
        if (t instanceof UnknownServiceException)
            return ApiErrorCodes.NO_INTERNET;
        if (t instanceof SocketTimeoutException)
            return ApiErrorCodes.NO_INTERNET;

        return ApiErrorCodes.OTHER_ERROR;
    }

    public ApiCalls getApi() {
        return apiCalls;
    }

    public enum ApiErrorCodes {
        TIME_OUT, NO_INTERNET, OTHER_ERROR,
    }
}

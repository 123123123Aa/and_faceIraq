package com.developersgroups.faceiraq.contact.us;

import com.developersgroups.faceiraq.api.ApiCalls;
import com.developersgroups.faceiraq.api.ApiManager;
import com.developersgroups.faceiraq.api.data.response.EmailResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Paweł Sałata on 04.07.2017.
 * email: psalata9@gmail.com
 */

class ContactUsController {

    private ApiCalls mApiCalls = ApiManager.get().getApi();

    public ContactUsController() {}

    Observable<EmailResponse> sendEmail(String subject, String message, String email) {
        return mApiCalls.sendEmail(subject, message, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

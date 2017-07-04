package com.developersgroups.faceiraq.utils;

import rx.Subscription;

/**
 * Created by Paweł Sałata on 04.07.2017.
 * email: psalata9@gmail.com
 */

public class RxUtil {
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}

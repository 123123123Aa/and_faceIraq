package com.ready4s.faceiraq.and_faceiraq.model.utils;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class PageUrlValidator {

    public final static String HTTP_PREFIX = "http://";

    public static String validatePageUrl(String rawUrl) {
        if (!rawUrl.contains(HTTP_PREFIX)) {
            return HTTP_PREFIX + rawUrl;
        }
        return rawUrl;
    }

}

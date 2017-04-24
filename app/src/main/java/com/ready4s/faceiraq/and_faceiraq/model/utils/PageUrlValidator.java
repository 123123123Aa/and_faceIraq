package com.ready4s.faceiraq.and_faceiraq.model.utils;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class PageUrlValidator {

    public final static String HTTP_PREFIX = "http://";
    public final static String HTTPS_PREFIX = "https://";

    public static String validatePageUrl(String rawUrl) {
        if (!rawUrl.isEmpty() && !rawUrl.contains(HTTP_PREFIX) && !rawUrl.contains(HTTPS_PREFIX)) {
            return HTTP_PREFIX + rawUrl;
        }
        return rawUrl;
    }

}

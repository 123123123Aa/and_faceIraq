package com.ready4s.faceiraq.and_faceiraq.model.utils;

/**
 * Created by Paweł Sałata on 14.04.2017.
 * email: psalata9@gmail.com
 */

public class PageUrlValidator {

    public final static String HTTP_PREFIX = "http://";
    public final static String HTTPS_PREFIX = "https://";
    public static final String WWW_PREFIX = "www.";

    public static String validatePageUrl(String rawUrl) {
        if (!rawUrl.isEmpty() && !rawUrl.contains(HTTP_PREFIX) && !rawUrl.contains(HTTPS_PREFIX)) {
            return HTTP_PREFIX + rawUrl;
        }
        return rawUrl;
    }

    public static String getRawUrl(String validUrl) {
        String rawUrl = validUrl;
        if (validUrl.startsWith(HTTPS_PREFIX)) {
            rawUrl = validUrl.replaceFirst(HTTPS_PREFIX, "");
        }
        if (validUrl.startsWith(HTTP_PREFIX)) {
            rawUrl = validUrl.replaceFirst(HTTP_PREFIX, "");
        }
        if (validUrl.isEmpty()) {
            return "";
        }
        if (!rawUrl.startsWith(WWW_PREFIX)) {
            rawUrl =  WWW_PREFIX + rawUrl;
        }
        return rawUrl;
    }

    public static boolean isValid(String url) {
        return url.startsWith(HTTP_PREFIX);
    }

}

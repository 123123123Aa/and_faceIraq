package com.developersgroups.faceiraq.utils;

import android.text.format.DateFormat;

/**
 * Created by Paweł Sałata on 18.04.2017.
 * email: psalata9@gmail.com
 */

public class TimeUtil {

    public static final long FACTOR = 1000L;
    public static final String HISTORY_FORMAT = "dd/MM/yyyy";
    public static final String TODAY_PREFIX = "TODAY ";

    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / FACTOR;
    }

    public static String getFormattedDateWithPrefix(long dateMillis, String dateFormat) {
        String dateString = getFormattedDate(dateMillis, dateFormat);
        String datePrefix = shouldHaveTodayPrefix(getCurrentTimestamp(), dateMillis) ? TODAY_PREFIX : "";
        return datePrefix + dateString;
    }

    private static boolean shouldHaveTodayPrefix(long currentTimestamp, long recordTimestamp) {
        return getFormattedDate(currentTimestamp, HISTORY_FORMAT)
                .equals(getFormattedDate(recordTimestamp, HISTORY_FORMAT));
    }

    private static String getFormattedDate(long dateMillis, String dateFormat) {
        return DateFormat.format(dateFormat, dateMillis * FACTOR).toString();
    }


}

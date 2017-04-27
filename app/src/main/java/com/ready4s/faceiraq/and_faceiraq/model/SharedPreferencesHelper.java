package com.ready4s.faceiraq.and_faceiraq.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ready4s.faceiraq.and_faceiraq.R;

/**
 * Created by Paweł Sałata on 24.04.2017.
 * email: psalata9@gmail.com
 */

public class SharedPreferencesHelper {

    private static String CARD_NUMBER_PREF = "selectedCardNumber";
    private static String CARD_URL_PREF = "selectedCardUrl";
    private static String THEME_NAME_PREF = "selectedThemeName";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("CardNumberAcrossApplication", context.MODE_PRIVATE);
    }

    public static long getCardNumber(Context context) {
        return getPrefs(context).getLong(CARD_NUMBER_PREF, 1);
    }

    public static void setCardNumber(Context context, long value) {
        getPrefs(context).edit().putLong(CARD_NUMBER_PREF, value).commit();
    }

    public static String getCardUrl(Context context) {
        return getPrefs(context).getString(CARD_URL_PREF, context.getString(R.string.HOME_PAGE_ADDRESS));
    }

    public static void setCardUrl(Context context, String value) {
        getPrefs(context).edit().putString(CARD_URL_PREF, value).commit();
    }

    public static String getThemeName(Context context) {
        return getPrefs(context).getString(THEME_NAME_PREF, context.getString(R.string.defaultThemeName));
    }

    public static void setThemeName(Context context, String value) {
        getPrefs(context).edit().putString(THEME_NAME_PREF, value).commit();
    }
}

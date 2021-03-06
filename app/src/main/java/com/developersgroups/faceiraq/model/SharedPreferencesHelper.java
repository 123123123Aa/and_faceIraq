package com.developersgroups.faceiraq.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.developersgroups.faceiraq.R;


/**
 * Created by Paweł Sałata on 24.04.2017.
 * email: psalata9@gmail.com
 */

public class SharedPreferencesHelper {

    private static String CARD_NUMBER_PREF = "selectedCardNumber";
    private static String CARD_URL_PREF = "selectedCardUrl";
    private static String THEME_NAME_PREF = "selectedThemeName";
    private static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "gcmRegistrationComplete";
    private static String IS_CHECKED_PREF = "Checked";
    private static String HAS_CHANGED_PREF = "Switch_Change";
    private static String UUID = "DeviceIdentification";

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("CardNumberAcrossApplication", context.MODE_PRIVATE);
    }

    public static long getCardNumber(Context context) {
        return getPrefs(context).getLong(CARD_NUMBER_PREF, 1);
    }

    public static void setCardNumber(Context context, long value) {
        getPrefs(context).edit().putLong(CARD_NUMBER_PREF, value).apply();
    }

    public static String getCardUrl(Context context) {
        return getPrefs(context).getString(CARD_URL_PREF, context.getString(R.string.HOME_PAGE_ADDRESS));
    }

    public static void setCardUrl(Context context, String value) {
        getPrefs(context).edit().putString(CARD_URL_PREF, value).apply();
    }

    public static String getThemeName(Context context) {
        return getPrefs(context).getString(THEME_NAME_PREF, context.getString(R.string.defaultThemeName));
    }

    public static void setThemeName(Context context, String value) {
        getPrefs(context).edit().putString(THEME_NAME_PREF, value).apply();
    }

    public static boolean isTokenSentToServer(Context context) {
        return getPrefs(context).getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public static void setTokenSentToServer(Context context, boolean value) {
        getPrefs(context).edit().putBoolean(SENT_TOKEN_TO_SERVER, value).apply();
    }

    public static void setChecked(Context context, boolean isChecked) {
        getPrefs(context).edit().putBoolean(IS_CHECKED_PREF, isChecked).commit();
    }

    public static boolean getChecked(Context context) {
        return getPrefs(context).getBoolean(IS_CHECKED_PREF, false);
    }

    public static void setChanged(Context context, boolean hasChanged) {
        getPrefs(context).edit().putBoolean(HAS_CHANGED_PREF, hasChanged).commit();
        
    }

    public static boolean getChanged(Context context) {
        return getPrefs(context).getBoolean(HAS_CHANGED_PREF, false);
    }

    public static void setUUID(Context context, String value) {
        getPrefs(context).edit().putString(UUID, value).apply();
    }

    public static String getUUID(Context context) {
        return getPrefs(context).getString(UUID, "");
    }
}

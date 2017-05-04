package net.faceiraq.and_faceiraq.model.utils;

import android.app.Activity;
import android.content.Intent;

import net.faceiraq.and_faceiraq.R;

import net.faceiraq.and_faceiraq.model.SharedPreferencesHelper;

/**
 * Created by user on 21.04.2017.
 */

public class ThemeChangeUtil {

    private static String sTheme = "Beige";


    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, String theme)
    {
        sTheme = theme;
        SharedPreferencesHelper.setThemeName(activity, theme);
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));

    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (SharedPreferencesHelper.getThemeName(activity))
        {
            default:
            case "Beige":
                activity.setTheme(R.style.beige_theme);
                break;
            case "Red":
                activity.setTheme(R.style.red_theme);
                break;
            case "Pink":
                activity.setTheme(R.style.pink_theme);
                break;
            case "Purple":
                activity.setTheme(R.style.purple_theme);
                break;
            case "Dark Blue":
                activity.setTheme(R.style.dark_blue_theme);
                break;
            case "Blue":
                activity.setTheme(R.style.blue_theme);
                break;
            case "Turquoise":
                activity.setTheme(R.style.turquoise_theme);
                break;
            case "Dark Green":
                activity.setTheme(R.style.dark_green_theme);
                break;
            case "Green":
                activity.setTheme(R.style.green_theme);
                break;
            case "Yellow":
                activity.setTheme(R.style.yellow_theme);
                break;
            case "Orange":
                activity.setTheme(R.style.orange_theme);
                break;
        }
    }


}

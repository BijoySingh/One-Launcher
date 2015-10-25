package com.bijoykochar.launcher.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The time functions and values
 * Created by bijoy on 10/17/15.
 */
public class TimeUtilities {
    public static final String TIME_FORMAT_24HR = "HH:mm";
    public static final String TIME_FORMAT_12HR = "hh:mm a";
    public static final String DATE_FORMAT = "EEEE, dd MMM";
    public static final String FULL_TIME_24HR = "EEEE, dd MMM 'at' HH:mm";
    public static final String FULL_TIME_12HR = "EEEE, dd MMM 'at' hh:mm a";

    /**
     * Sets the time format to 12 hours
     *
     * @param context the context
     */
    public static void setTimeFormat12hr(Context context) {
        Preferences.getInstance(context).save(Preferences.Keys.TIME_FORMAT, TIME_FORMAT_12HR);
    }

    /**
     * Sets the time format to 24 hours
     *
     * @param context the context
     */
    public static void setTimeFormat24hr(Context context) {
        Preferences.getInstance(context).save(Preferences.Keys.TIME_FORMAT, TIME_FORMAT_24HR);
    }

    /**
     * Return the default time format
     *
     * @param context the context
     * @return the format
     */
    public static SimpleDateFormat getTimeFormat(Context context) {
        return new SimpleDateFormat(
                Preferences.getInstance(context).load(Preferences.Keys.TIME_FORMAT, TIME_FORMAT_12HR),
                Locale.getDefault());
    }

    /**
     * Return the default date format
     *
     * @param context the context
     * @return the format
     */
    public static SimpleDateFormat getDateFormat(Context context) {
        return new SimpleDateFormat(
                Preferences.getInstance(context).load(Preferences.Keys.DATE_FORMAT, DATE_FORMAT),
                Locale.getDefault());
    }

    /**
     * Return the default ful time format
     *
     * @param context the context
     * @return the format
     */
    public static SimpleDateFormat getFullTimeFormat(Context context) {
        String timeFormat = Preferences.getInstance(context).load(Preferences.Keys.TIME_FORMAT, TIME_FORMAT_12HR);
        if (timeFormat.contentEquals(TIME_FORMAT_12HR)) {
            return new SimpleDateFormat(FULL_TIME_12HR, Locale.getDefault());
        } else {
            return new SimpleDateFormat(FULL_TIME_24HR, Locale.getDefault());
        }
    }
}

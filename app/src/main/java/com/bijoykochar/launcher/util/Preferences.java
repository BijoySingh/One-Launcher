package com.bijoykochar.launcher.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.bijoykochar.launcher.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores and loads from the shared preferences
 * Created by bijoy on 10/9/15.
 */
public class Preferences {

    Context context;
    public static final String SHARED_PREFERENCES = "launcher";

    public static final class Keys {
        public static final String TIME_FORMAT = "TIME_FORMAT";
        public static final String DATE_FORMAT = "DATE_FORMAT";
        public static final String SETTINGS_APP_LIST = "SETTINGS_APP_LIST";
        public static final String SETTINGS_SOMETHING_CHANGED = "SETTINGS_SOMETHING_CHANGED";
        public static final String SETTINGS_SHOW_CALLS = "SETTINGS_SHOW_CALLS";
        public static final String SETTINGS_SHOW_MESSAGES = "SETTINGS_SHOW_MESSAGES";
        public static final String SETTINGS_SHOW_CALENDAR = "SETTINGS_SHOW_CALENDAR";
        public static final String SETTINGS_SHOW_MUSIC = "SETTINGS_SHOW_MUSIC";
        public static final String TRUE = "TRUE";
        public static final String FALSE = "FALSE";
    }

    /**
     * Load the data from the shared preferences
     *
     * @param key the key of the data
     * @return the value stored or a default
     */
    public String load(String key) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES,
                Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * Load the data from the shared preferences
     *
     * @param key          the key of the data
     * @param defaultValue the default value
     * @return the value stored or a default
     */
    public String load(String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES,
                Activity.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    /**
     * Saves the data into the shared preferences
     *
     * @param key   the key of the data
     * @param value the value to store
     */
    public void save(String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Saves a boolean variable
     *
     * @param key  the key
     * @param bool the bool to store
     */
    public void saveBoolean(String key, Boolean bool) {
        if (bool) {
            save(key, Keys.TRUE);
        } else {
            save(key, Keys.FALSE);
        }
    }

    /**
     * Gets the stored boolean value
     *
     * @param key         the key
     * @param defaultBool boolean if it wasnt stored before or is not a bool
     * @return the stored value as boolean
     */
    public Boolean loadBoolean(String key, Boolean defaultBool) {
        String stored = load(key);
        if (stored.contentEquals(Keys.TRUE)) {
            return true;
        } else if (stored.contentEquals(Keys.FALSE)) {
            return false;
        } else {
            return defaultBool;
        }
    }

    /**
     * Private Constructor
     *
     * @param context activity context
     */
    private Preferences(Context context) {
        this.context = context;
    }

    /**
     * Factory method to give the preferences
     *
     * @param context activity context
     * @return the preference object
     */
    public static Preferences getInstance(Context context) {
        return new Preferences(context);
    }

    /**
     * Resets the value in the key
     *
     * @param key the key
     */
    public void reset(String key) {
        save(key, "");
    }

    /**
     * Returns the settings values
     *
     * @return
     */
    public Map<String, Boolean> getSettings() {
        Map<String, Boolean> settings = new HashMap<>();
        settings.put(Keys.SETTINGS_APP_LIST, loadBoolean(Keys.SETTINGS_APP_LIST, false));
        settings.put(Keys.SETTINGS_SHOW_CALENDAR, loadBoolean(Keys.SETTINGS_SHOW_CALENDAR, true));
        settings.put(Keys.SETTINGS_SHOW_MESSAGES, loadBoolean(Keys.SETTINGS_SHOW_MESSAGES, true));
        settings.put(Keys.SETTINGS_SHOW_CALLS, loadBoolean(Keys.SETTINGS_SHOW_CALLS, true));
        settings.put(Keys.SETTINGS_SHOW_MUSIC, loadBoolean(Keys.SETTINGS_SHOW_MUSIC, false));
        settings.put(Keys.SETTINGS_SOMETHING_CHANGED, loadBoolean(Keys.SETTINGS_SOMETHING_CHANGED, true));

        return settings;
    }

    public int getAppIcon() {
        if (loadBoolean(Keys.SETTINGS_APP_LIST, false)) {
            return R.drawable.ic_view_list_white_36dp;
        } else {
            return R.drawable.ic_grid_on_white_36dp;
        }
    }
}

package com.bijoykochar.launcher.items;

import android.view.View;

/**
 * Created by bijoy on 10/20/15.
 */
public class SettingsItem {

    public class Tags {
        public static final String APP_LIST = "APP_LIST";
        public static final String SHOW_CALENDAR = "SHOW_CALENDAR";
        public static final String SHOW_CALLS = "SHOW_CALLS";
        public static final String SHOW_MESSAGES = "SHOW_MESSAGES";
        public static final String SHOW_MUSIC = "SHOW_MUSIC";
        public static final String SHOW_RECENT = "SHOW_RECENT";
        public static final String CHANGE_WALLPAPER = "CHANGE_WALLPAPER";
    }

    public String tag;
    public String title, subtitle;
    public Integer logo;
    public Boolean showCheckbox, isChecked;
    public Boolean haveMargin;

    public SettingsItem(String tag, String title, String subtitle, Integer logo, Boolean showCheckbox) {
        this.tag = tag;
        this.title = title;
        this.subtitle = subtitle;
        this.logo = logo;
        this.showCheckbox = showCheckbox;
        this.isChecked = false;
        this.haveMargin = false;
    }

    public SettingsItem(String tag, String title, String subtitle, Integer logo, Boolean showCheckbox, Boolean isChecked) {
        this.tag = tag;
        this.title = title;
        this.subtitle = subtitle;
        this.logo = logo;
        this.showCheckbox = showCheckbox;
        this.isChecked = isChecked;
        this.haveMargin = false;
    }

    public SettingsItem setShowMargin() {
        this.haveMargin = true;
        return this;
    }
}

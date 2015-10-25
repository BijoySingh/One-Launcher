package com.bijoykochar.launcher;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bijoykochar.launcher.adapter.SettingsAdapter;
import com.bijoykochar.launcher.items.SettingsItem;
import com.bijoykochar.launcher.util.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SettingsActivity extends Activity {

    Context context;
    RecyclerView.LayoutManager layoutManager;
    SettingsAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;
        initializeRecyclerView();
    }

    public void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SettingsAdapter(context, getSettings(), R.layout.settings_item);
        recyclerView.setAdapter(adapter);
    }

    private List<SettingsItem> getSettings() {
        final Preferences preferences = Preferences.getInstance(context);
        Map<String, Boolean> appSettings = preferences.getSettings();

        List<SettingsItem> settings = new ArrayList<>();
        settings.add(new SettingsItem(SettingsItem.Tags.CHANGE_WALLPAPER, "Change Wallpaper",
                "Change your homescreen wallpaper", R.drawable.ic_wallpaper_white_36dp, false));
        if (Build.VERSION.SDK_INT >= 21) {
            settings.add(new SettingsItem(SettingsItem.Tags.SHOW_RECENT, "Show Recent Apps",
                    "Show your top used apps as suggestion", R.drawable.ic_access_time_white_36dp, false));
        }
        settings.add(new SettingsItem(SettingsItem.Tags.APP_LIST, "Apps View",
                "Choose the view for your app list", preferences.getAppIcon(), true,
                appSettings.get(Preferences.Keys.SETTINGS_APP_LIST)));
        settings.add(new SettingsItem(SettingsItem.Tags.SHOW_MESSAGES, "Show Messages",
                "Show the recent unread messages", R.drawable.ic_message_white_48dp, true,
                appSettings.get(Preferences.Keys.SETTINGS_SHOW_MESSAGES)));
        settings.add(new SettingsItem(SettingsItem.Tags.SHOW_CALLS, "Show Calls",
                "Show the recent calls", R.drawable.ic_phone_white_48dp, true,
                appSettings.get(Preferences.Keys.SETTINGS_SHOW_CALLS)));
        settings.add(new SettingsItem(SettingsItem.Tags.SHOW_CALENDAR, "Show Events",
                "Show the upcoming calendar messages", R.drawable.ic_event_white_48dp, true,
                appSettings.get(Preferences.Keys.SETTINGS_SHOW_CALENDAR)));
        settings.add(new SettingsItem(SettingsItem.Tags.SHOW_MUSIC, "Show Music",
                "Show the available musics", R.drawable.ic_music_note_white_48dp, true,
                appSettings.get(Preferences.Keys.SETTINGS_SHOW_MUSIC)));
        return settings;
    }

}

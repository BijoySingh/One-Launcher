package com.bijoykochar.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SettingsItem;
import com.bijoykochar.launcher.util.Preferences;
import com.bijoykochar.launcher.view.SettingsViewHolder;

import java.util.List;

/**
 * The recycler adapter for apps
 * Created by bijoy on 8/4/15.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {

    private Context context;
    private List<SettingsItem> settings;
    private int layout;
    private Preferences preferences;

    public SettingsAdapter(Context context,
                           List<SettingsItem> settings,
                           int layout

    ) {
        this.preferences = Preferences.getInstance(context);
        this.context = context;
        this.settings = settings;
        this.layout = layout;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new SettingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, final int position) {

        final SettingsItem data = settings.get(position);
        holder.title.setText(data.title);
        holder.subtitle.setText(data.subtitle);
        holder.logo.setVisibility(View.VISIBLE);
        holder.logo.setImageResource(data.logo);

        holder.logo.setColorFilter(context.getResources().getColor(R.color.drawer_text));

        if (data.showCheckbox) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(data.isChecked);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (data.tag) {
                    case SettingsItem.Tags.APP_LIST:
                        preferences.saveBoolean(Preferences.Keys.SETTINGS_APP_LIST, isChecked);
                        break;
                    case SettingsItem.Tags.SHOW_CALENDAR:
                        preferences.saveBoolean(Preferences.Keys.SETTINGS_SHOW_CALENDAR, isChecked);
                        break;
                    case SettingsItem.Tags.SHOW_MESSAGES:
                        preferences.saveBoolean(Preferences.Keys.SETTINGS_SHOW_MESSAGES, isChecked);
                        break;
                    case SettingsItem.Tags.SHOW_MUSIC:
                        preferences.saveBoolean(Preferences.Keys.SETTINGS_SHOW_MUSIC, isChecked);
                        break;
                    case SettingsItem.Tags.SHOW_CALLS:
                        preferences.saveBoolean(Preferences.Keys.SETTINGS_SHOW_CALLS, isChecked);
                        break;
                }
                preferences.saveBoolean(Preferences.Keys.SETTINGS_SOMETHING_CHANGED, true);
            }
        });
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.tag.equals(SettingsItem.Tags.CHANGE_WALLPAPER)) {
                    preferences.saveBoolean(Preferences.Keys.SETTINGS_SOMETHING_CHANGED, true);
                    Intent changeWallpaper = new Intent(Intent.ACTION_SET_WALLPAPER);
                    context.startActivity(changeWallpaper);
                } else if (data.tag.equals(SettingsItem.Tags.SHOW_RECENT)) {
                    Intent permissionSet = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    context.startActivity(permissionSet);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

}
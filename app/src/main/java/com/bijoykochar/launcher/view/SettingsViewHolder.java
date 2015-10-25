package com.bijoykochar.launcher.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bijoykochar.launcher.R;

/**
 * The view holder for the viewing of apps
 * Created by bijoy on 8/4/15.
 */
public class SettingsViewHolder extends RecyclerView.ViewHolder {
    public View rootView;
    public TextView title, subtitle;
    public ImageView logo;
    public CheckBox checkBox;

    public SettingsViewHolder(View v) {
        super(v);
        rootView = v;
        title = (TextView) v.findViewById(R.id.title);
        subtitle = (TextView) v.findViewById(R.id.subtitle);
        logo = (ImageView) v.findViewById(R.id.logo);
        checkBox = (CheckBox) v.findViewById(R.id.checkbox);
    }

}

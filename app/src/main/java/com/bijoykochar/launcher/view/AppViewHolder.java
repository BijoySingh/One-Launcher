package com.bijoykochar.launcher.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bijoykochar.launcher.R;

/**
 * The view holder for the viewing of apps
 * Created by bijoy on 8/4/15.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {
    public View rootView;
    public TextView title;
    public ImageView logo;

    public AppViewHolder(View v) {
        super(v);
        rootView = v;
        title = (TextView) v.findViewById(R.id.title);
        logo = (ImageView) v.findViewById(R.id.logo);
    }

}

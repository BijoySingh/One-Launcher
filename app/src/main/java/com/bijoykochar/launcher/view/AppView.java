package com.bijoykochar.launcher.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.AppDetail;

/**
 * Created by bijoy on 10/20/15.
 */
public class AppView extends LinearLayout {

    public TextView title;
    public ImageView logo;

    public AppView(Context context) {
        super(context);
    }

    public AppView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AppView init(Context context, int layout) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(layout, this, true);

        title = (TextView) rootView.findViewById(R.id.title);
        logo = (ImageView) rootView.findViewById(R.id.logo);

        return this;
    }

    public void setApp(AppDetail app, PackageManager manager) {
        this.title.setText(app.label);
        try {
            this.logo.setImageDrawable(manager.getApplicationIcon(app.name));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(AppView.class.getSimpleName(), e.getMessage(), e);
        }
    }
}

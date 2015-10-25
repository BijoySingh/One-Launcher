package com.bijoykochar.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.fragment.AppsFragment;
import com.bijoykochar.launcher.fragment.RecyclerFragment;
import com.bijoykochar.launcher.items.AppDetail;
import com.bijoykochar.launcher.view.AppViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * The recycler adapter for apps
 * Created by bijoy on 8/4/15.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private Context context;
    private AppsFragment fragment;
    private PackageManager manager;
    private int layout;

    public AppListAdapter(Context context,
                          AppsFragment fragment,
                          PackageManager manager,
                          int layout

    ) {
        this.context = context;
        this.fragment = fragment;
        this.manager = manager;
        this.layout = layout;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new AppViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {

        final AppDetail data = fragment.getValues().get(position);
        if (data != null) {
            holder.title.setText(data.label);
            holder.logo.setVisibility(View.VISIBLE);
            try {
                if (!fragment.icons.containsKey(data.name)) {
                    fragment.icons.put(data.name, manager.getApplicationIcon(data.name));
                }
                holder.logo.setImageDrawable(fragment.icons.get(data.name));
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(AppListAdapter.class.getSimpleName(), e.getMessage(), e);
            }

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.refreshList();
                    Intent i = manager.getLaunchIntentForPackage(data.name);
                    context.startActivity(i);
                    Log.d(data.name, data.label);
                }
            });
            holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    fragment.showSnackbar(data);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fragment.getValues().size();
    }

}
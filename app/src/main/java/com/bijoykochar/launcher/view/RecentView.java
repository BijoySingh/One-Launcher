package com.bijoykochar.launcher.view;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.comparators.AppMostUsedReverseComparator;
import com.bijoykochar.launcher.items.AppDetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The view for the home recent apps
 * Created by bijoy on 10/17/15.
 */
public class RecentView extends LinearLayout {

    private static final String USAGE_SERVICE = "usagestats";
    private static final Integer MAX_COUNT = 100;
    private LinearLayout[] layers = new LinearLayout[2];
    private boolean hasPermission = true;

    List<AppDetail> recentApps;

    public RecentView(Context context) {
        super(context);
    }

    public RecentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.recent_apps, this, true);

        layers[0] = (LinearLayout) rootView.findViewById(R.id.layer_1);
        layers[1] = (LinearLayout) rootView.findViewById(R.id.layer_2);
    }

    /**
     * Returns recent packages for versions over lollipop
     *
     * @param context the context
     * @return the packagers
     */
    @TargetApi(21)
    public List<String> getRecentPackages(Context context) {
        final UsageStatsManager manager = (UsageStatsManager) context.getSystemService(USAGE_SERVICE);
        Calendar beginCal = Calendar.getInstance();
        beginCal.add(Calendar.YEAR, -1);


        List<UsageStats> stats =
                manager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY,
                        beginCal.getTimeInMillis(), System.currentTimeMillis());
        Collections.sort(stats, new AppMostUsedReverseComparator());

        List<String> packages = new ArrayList<>();
        for (UsageStats stat : stats) {
            packages.add(stat.getPackageName());
        }

        if (stats.isEmpty()) {
            hasPermission = false;
        } else {
            hasPermission = true;
        }

        return packages;
    }

    /**
     * Returns recent packages for older versions
     *
     * @param context the context
     * @return the packagers
     */
    public List<String> getOldRecentPackages(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> tasks = manager.getRecentTasks(20, 0);

        List<String> packages = new ArrayList<>();

        for (ActivityManager.RecentTaskInfo task : tasks) {
            packages.add(task.baseIntent.getComponent().getPackageName());

        }

        return packages;
    }

    /**
     * Returns the list of recent apps
     *
     * @param apps    map from package name to apps fro alla apps
     * @param context the context
     * @return the list of apps
     */
    public List<AppDetail> getRecentApps(Map<String, AppDetail> apps, Context context) {
        List<String> packages;
        if (Build.VERSION.SDK_INT >= 21) {
            packages = getRecentPackages(context);
        } else {
            packages = getOldRecentPackages(context);
        }

        List<AppDetail> recentApps = new ArrayList<>();
        Set<String> recentPackages = new HashSet<>();
        for (String packageName : packages) {
            if (recentApps.size() >= MAX_COUNT) {
                break;
            }

            if (apps.containsKey(packageName) && !recentPackages.contains(packageName)
                    && !packageName.contentEquals(context.getPackageName())) {
                recentApps.add(apps.get(packageName));
                recentPackages.add(packageName);
            }
        }

        return recentApps;
    }

    /**
     * Creates the list of apps
     *
     * @param context the context
     */
    public void createAppList(final Context context, int layerCount, int appLayout) {
        int position = 0;
        for (LinearLayout layout : layers) {
            layout.removeAllViews();
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        if (hasPermission) {
            final PackageManager packageManager = context.getPackageManager();
            for (final AppDetail app : recentApps) {
                AppView appView = (new AppView(context)).init(context, appLayout);
                appView.setLayoutParams(params);
                appView.setApp(app, packageManager);

                if (position >= 2 * layerCount) {
                    return;
                } else if (position >= layerCount) {
                    layers[1].addView(appView);
                } else {
                    layers[0].addView(appView);
                }
                position++;

                appView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = packageManager.getLaunchIntentForPackage(app.name);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    public void getData(Context context, List<AppDetail> apps) {
        recentApps = getRecentApps(AppDetail.getMapping(apps), context);
    }
}

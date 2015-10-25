package com.bijoykochar.launcher.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bijoykochar.launcher.HomeActivity;
import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.AppDetail;
import com.bijoykochar.launcher.util.Preferences;
import com.bijoykochar.launcher.util.TimeUtilities;
import com.bijoykochar.launcher.util.Utilities;
import com.bijoykochar.launcher.view.RecentView;
import com.bijoykochar.launcher.view.TaskbarView;
import com.bijoykochar.launcher.view.TimeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Empty page - page under construction fragment, good for prototyping flow
 */
@SuppressLint("NewApi")
public class HomeFragment extends Fragment {

    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;

    TimeView timeView;
    TaskbarView taskbarView;
    RecentView recentView;

    Context context;
    List<AppDetail> apps = new ArrayList<>();
    Map<String, Boolean> settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_page, container, false);
        settings = Preferences.getInstance(getActivity()).getSettings();

        /**
         * Create the formatting
         */
        timeFormat = TimeUtilities.getTimeFormat(getActivity());
        dateFormat = TimeUtilities.getDateFormat(getActivity());

        /**
         * The time view initialization
         */
        timeView = (TimeView) rootView.findViewById(R.id.time_view);
        timeView.init(getActivity());
        setupTimeRefreshHandler();

        /**
         * The taskbar view initialization
         */
        setupRecentApps(rootView);
        setupTaskbar(rootView);

        return rootView;
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        timeView.setTime(time, date);
    }

    private void setupTimeRefreshHandler() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                setTime();
                handler.postDelayed(this, 500);
            }
        });
    }

    private void setupTaskbar(View rootView) {
        taskbarView = (TaskbarView) rootView.findViewById(R.id.taskbar_view);
        taskbarView.init(getActivity());
        taskbarView.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).setCurrentPage(HomeActivity.APP_LIST);
            }
        });
        taskbarView.menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ((HomeActivity) getActivity()).setCurrentPage(HomeActivity.ADDITIONAL_INFORMATION);
                return true;
            }
        });
        taskbarView.createAppList(getActivity());
    }

    private void setupRecentApps(View rootView) {
        recentView = (RecentView) rootView.findViewById(R.id.recent_view);
        recentView.init(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        getSetupTask().execute();
    }

    public void setApps(List<AppDetail> apps) {
        this.apps = apps;
        getSetupTask().execute();
    }

    AsyncTask<Void, Void, Void> getSetupTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                if (recentView != null) {
                    recentView.getData(context, apps);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (recentView != null ) {
                    double screenSize = Utilities.getScreenSizeInInches(getActivity());
                    if (screenSize < 6.0) {
                        recentView.createAppList(context, 4, R.layout.app_view_grid);
                    } else {
                        recentView.createAppList(context, 6, R.layout.app_view_grid_large);
                    }
                }
            }
        };
    }

}


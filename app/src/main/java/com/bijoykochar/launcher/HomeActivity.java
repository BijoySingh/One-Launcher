package com.bijoykochar.launcher;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.bijoykochar.launcher.comparators.AppAlphabeticalComparator;
import com.bijoykochar.launcher.fragment.AppsFragment;
import com.bijoykochar.launcher.fragment.HomeFragment;
import com.bijoykochar.launcher.fragment.InformationPageFragment;
import com.bijoykochar.launcher.items.AppDetail;
import com.bijoykochar.launcher.util.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HomeActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;
    private static final int CACHED_PAGES = 6;
    public List<AppDetail> apps = new ArrayList<>();

    public static final Integer ADDITIONAL_INFORMATION = 0;
    public static final Integer DEFAULT_STATE = 1;
    public static final Integer APP_LIST = 2;

    /**
     * The package manager
     */
    private PackageManager manager;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    private HomeFragment homeFragment;
    private AppsFragment appsFragment;
    private InformationPageFragment informationPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        manager = getPackageManager();

        setWallpaper();

        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(CACHED_PAGES);

        transparentNavbar();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() != DEFAULT_STATE) {
            pager.setCurrentItem(DEFAULT_STATE);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == ADDITIONAL_INFORMATION) {
                if (informationPageFragment == null) {
                    informationPageFragment = new InformationPageFragment();
                }
                return informationPageFragment;
            } else if (position == DEFAULT_STATE) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                return homeFragment;
            } else if (position == APP_LIST) {
                if (appsFragment == null) {
                    appsFragment = new AppsFragment();
                }

                appsFragment.setApps(apps);
                return appsFragment;
            }

            return new HomeFragment();
        }


        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @TargetApi(16)
    private void setWallpaper() {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.home_frame);
        frameLayout.setBackground(wallpaperDrawable);
    }

    /**
     * Load the list of apps
     */
    private void loadApps() {
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        apps.clear();

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for (ResolveInfo ri : availableActivities) {
            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager).toString();
            app.name = ri.activityInfo.packageName;
            apps.add(app);
        }

        Collections.sort(apps, new AppAlphabeticalComparator());
    }

    AsyncTask<Void, Void, Void> getSetupTask() {
        return new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                loadApps();

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (appsFragment != null) {
                    appsFragment.setApps(apps);
                }
                if (homeFragment != null) {
                    homeFragment.setApps(apps);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSetupTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.setCurrentItem(DEFAULT_STATE);
        if (Preferences.getInstance(this).loadBoolean(Preferences.Keys.SETTINGS_SOMETHING_CHANGED, true)) {
            setWallpaper();
        }
    }

    /**
     * Sets the current page
     *
     * @param position the new position
     */
    public void setCurrentPage(int position) {
        pager.setCurrentItem(position);
    }

    public void transparentNavbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}

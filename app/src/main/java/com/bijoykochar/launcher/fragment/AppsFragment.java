package com.bijoykochar.launcher.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.SettingsActivity;
import com.bijoykochar.launcher.adapter.AppListAdapter;
import com.bijoykochar.launcher.items.AppDetail;
import com.bijoykochar.launcher.util.Preferences;
import com.bijoykochar.launcher.util.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Empty page - page under construction fragment, good for prototyping flow
 */
@SuppressLint("NewApi")
public class AppsFragment extends RecyclerFragment<AppDetail> {

    private List<AppDetail> apps = new ArrayList<>();
    public Map<String, Drawable> icons = new HashMap<>();
    private PackageManager manager;
    private AppListAdapter adapter;
    private EditText searchBox;
    private ImageView crossButton;
    private View coordinatorLayoutView;
    private boolean isList = false;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_page, container, false);
        manager = getActivity().getPackageManager();

        coordinatorLayoutView = rootView.findViewById(R.id.snackbar);
        setupRefresh(rootView);
        setupRecyclerView(rootView);
        setSearchBox(rootView);
        setupAppIcons(rootView);

        return rootView;
    }

    public void setupRecyclerView(View rootView) {
        double screenSize = Utilities.getScreenSizeInInches(getActivity());
        if (Preferences.getInstance(getActivity()).loadBoolean(Preferences.Keys.SETTINGS_APP_LIST, false)){
            isList = true;
            setupRecyclerListView(rootView);
        } else if (screenSize < 6.0) {
            isList = false;
            setupRecyclerGridView(rootView, 4, R.layout.app_view_grid);
        } else {
            isList = false;
            setupRecyclerGridView(rootView, 5, R.layout.app_view_grid_large);
        }
    }

    /**
     * This function sets up the adapter, and the recycler view
     *
     * @param rootView the view of the fragment
     */
    public void setupRecyclerListView(View rootView) {
        initializeRecyclerView(rootView);
        adapter = new AppListAdapter(context, this, manager, R.layout.app_view);
        recyclerView.setAdapter(adapter);
    }

    /**
     * This function sets up the adapter, and the recycler view
     *
     * @param rootView the view of the fragment
     */
    public void setupRecyclerGridView(View rootView, int columns, int layout) {
        initializeRecyclerGridView(rootView, columns);
        adapter = new AppListAdapter(context, this, manager, layout);
        recyclerView.setAdapter(adapter);
    }

    public void refreshData() {
        if (!isList && Preferences.getInstance(getActivity()).loadBoolean(Preferences.Keys.SETTINGS_APP_LIST, false)) {
            setupRecyclerView(getView());
        } else if (isList && !Preferences.getInstance(getActivity()).loadBoolean(Preferences.Keys.SETTINGS_APP_LIST, false)) {
            setupRecyclerView(getView());
        }

        clearSearchBox();
        refreshList();
    }

    public void setupRefresh(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void refreshList() {
        values = new ArrayList<>(apps);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Filters the list to the search text
     *
     * @param search the text to search
     */
    private void searchList(String search) {
        values = AppDetail.getFiltered(values, search);
        adapter.notifyDataSetChanged();
    }

    private void setSearchBox(View rootView) {
        searchBox = (EditText) rootView.findViewById(R.id.search_box);
        crossButton = (ImageView) rootView.findViewById(R.id.delete_search_box);
        crossButton.setVisibility(View.GONE);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString();
                if (search.isEmpty()) {
                    refreshList();
                    crossButton.setVisibility(View.GONE);
                } else {
                    crossButton.setVisibility(View.VISIBLE);
                    values.clear();
                    values.addAll(apps);
                    searchList(search);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSearchBox();
                searchBox.clearFocus();
            }
        });
    }

    /**
     * Clears the search box
     */
    private void clearSearchBox() {
        searchBox.setText("");
    }

    /**
     * Sets up the icons on the app title
     *
     * @param rootView the views
     */
    private void setupAppIcons(View rootView) {
        ImageView playStore = (ImageView) rootView.findViewById(R.id.playstore);
        ImageView settings = (ImageView) rootView.findViewById(R.id.settings);

        playStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = manager.getLaunchIntentForPackage("com.android.vending");
                    startActivity(i);
                } catch (Exception e) {
                    Log.e(AppsFragment.class.getSimpleName(), e.getMessage(), e);
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(context, SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        settings.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent settingsIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                startActivity(settingsIntent);
                return true;
            }
        });
    }

    public void setApps(List<AppDetail> apps) {
        this.apps = apps;
        refreshList();
    }

    public void showSnackbar(final AppDetail appDetail) {
        final View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View v) {
                Uri packageURI = Uri.parse("package:" + appDetail.name);
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
            }
        };

        Snackbar snackbar = Snackbar.make(coordinatorLayoutView, "Would you like to uninstall '" + appDetail.label + "'?", Snackbar.LENGTH_LONG)
                .setAction(R.string.uninstall_app, clickListener)
                .setActionTextColor(Color.parseColor("#B2DFDB"));
        snackbar.getView().setBackgroundColor(Color.parseColor("#455A64"));
        snackbar.show();
    }

}


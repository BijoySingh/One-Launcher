package com.bijoykochar.launcher.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bijoykochar.launcher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The recycler fragment a base class for recycler view fragment
 * Created by bijoy on 8/4/15.
 */
public abstract class RecyclerFragment<T> extends Fragment {

    public List<T> values = new ArrayList<>();
    Context context;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    public void initializeRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void initializeRecyclerGridView(View rootView, int columns) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);

        layoutManager = new GridLayoutManager(context, columns);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void refreshList() {
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }
}
package com.bijoykochar.launcher.comparators;

import com.bijoykochar.launcher.items.AppDetail;

import java.util.Comparator;
import java.util.Locale;

/**
 * The comparator to sort a list of app details in alphabetical order
 * Created by bijoy on 10/17/15.
 */
public class AppAlphabeticalComparator implements Comparator<AppDetail> {
    @Override
    public int compare(AppDetail lhs, AppDetail rhs) {
        return lhs.label.toLowerCase(Locale.getDefault()).compareTo(rhs.label.toLowerCase(Locale.getDefault()));
    }
}

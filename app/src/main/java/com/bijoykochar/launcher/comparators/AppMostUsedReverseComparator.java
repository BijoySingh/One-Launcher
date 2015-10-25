package com.bijoykochar.launcher.comparators;

import android.annotation.TargetApi;
import android.app.usage.UsageStats;

import java.util.Comparator;

/**
 * The last time used comparator
 * Created by bijoy on 10/18/15.
 */
public class AppMostUsedReverseComparator implements Comparator<UsageStats> {

    @TargetApi(21)
    @Override
    public int compare(UsageStats lhs, UsageStats rhs) {
        Long lhsLastUser = lhs.getTotalTimeInForeground();
        Long rhsLastUser = rhs.getTotalTimeInForeground();
        return rhsLastUser.compareTo(lhsLastUser);
    }
}

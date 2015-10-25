package com.bijoykochar.launcher.items;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * THe item to store app information
 * Created by bijoy on 10/17/15.
 */
public class AppDetail {
    public String label;
    public String name;

    public static List<AppDetail> getFiltered(List<AppDetail> apps, String search) {
        List<AppDetail> filtered = new ArrayList<>();
        for (AppDetail app : apps) {
            if (app.label.toLowerCase().contains(search.toLowerCase())) {
                filtered.add(app);
            }
        }
        return filtered;
    }

    public static Map<String, AppDetail> getMapping(List<AppDetail> apps) {
        Map<String, AppDetail> mapping = new HashMap<>();
        for (AppDetail app : apps) {
            mapping.put(app.name, app);
        }
        return mapping;
    }
}

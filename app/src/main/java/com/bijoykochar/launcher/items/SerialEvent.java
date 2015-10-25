package com.bijoykochar.launcher.items;

import java.io.Serializable;

import me.everything.providers.android.calendar.Event;

/**
 * Created by bijoy on 10/19/15.
 */
public class SerialEvent implements Serializable {
    public String title, description;
    public long id, dTStart;

    public SerialEvent(Event event) {
        title = event.title;
        description = event.description;
        id = event.id;
        dTStart = event.dTStart;
    }
}

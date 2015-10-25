package com.bijoykochar.launcher.view;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SerialEvent;
import com.bijoykochar.launcher.util.TimeUtilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

/**
 * The view for the home recent apps
 * Created by bijoy on 10/17/15.
 */
public class CalendarView extends LinearLayout {

    View rootView;
    TextView title, description, dateTime;
    SerialEvent event;

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<SerialEvent> getSerialEvents(Context context) {
        CalendarProvider provider = new CalendarProvider(context);
        List<Calendar> calendars = provider.getCalendars().getList();

        List<Event> events = new ArrayList<>();
        for (Calendar calendar : calendars) {
            events.addAll(provider.getEvents(calendar.id).getList());
        }

        List<SerialEvent> sEvents = new ArrayList<>();
        for (Event event : events) {
            sEvents.add(new SerialEvent(event));
        }
        return sEvents;
    }

    public SerialEvent getNextEvent(Context context) {
        List<SerialEvent> events = getSerialEvents(context);

        if (!events.isEmpty()) {
            long timeInMillis = java.util.Calendar.getInstance().getTimeInMillis();
            long minTimeInMillis = timeInMillis + 1000 * 60 * 60 * 24 * 7 * 52;
            SerialEvent nextEvent = null;
            for (SerialEvent event : events) {
                if (event.dTStart > timeInMillis && event.dTStart < minTimeInMillis) {
                    nextEvent = event;
                    minTimeInMillis = event.dTStart;
                }
            }
            return nextEvent;
        }

        return null;
    }

    public void init(final Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.calendar_view, this, true);

        title = (TextView) rootView.findViewById(R.id.title);
        description = (TextView) rootView.findViewById(R.id.description);
        dateTime = (TextView) rootView.findViewById(R.id.date_time);

    }

    public void getData(Context context) {
        event = getNextEvent(context);
    }

    public void displayEvents(final Context context) {
        try {
            title.setText(event.title);
            description.setText(event.description);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(event.dTStart);
            SimpleDateFormat dateFormat = TimeUtilities.getFullTimeFormat(context);
            String dateAndTime = dateFormat.format(calendar.getTime());

            dateTime.setText(dateAndTime);

            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, event.id);
                    Intent intent = new Intent(Intent.ACTION_VIEW)
                            .setData(uri);
                    context.startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.e("CalendarView", e.getMessage(), e);
            setVisibility(View.GONE);
        }
    }
}

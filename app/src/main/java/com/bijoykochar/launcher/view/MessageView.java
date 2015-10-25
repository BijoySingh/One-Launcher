package com.bijoykochar.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SerialSms;
import com.bijoykochar.launcher.util.TimeUtilities;

import java.text.SimpleDateFormat;
import java.util.List;

import me.everything.providers.android.telephony.Sms;
import me.everything.providers.android.telephony.TelephonyProvider;

/**
 * The view for the home recent apps
 * Created by bijoy on 10/17/15.
 */
public class MessageView extends LinearLayout {

    View rootView;
    TextView source, message, dateTime;
    SerialSms sms;

    public MessageView(Context context) {
        super(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SerialSms getLastUnreadSms(Context context) {
        TelephonyProvider provider = new TelephonyProvider(context);
        List<Sms> smses = provider.getSms(TelephonyProvider.Filter.ALL).getList();

        if (!smses.isEmpty()) {
            long maxReadTimeInMillis = 0;
            long maxUnreadTimeInMillis = 0;

            Sms lastUnreadSms = null, lastReadSms = null;
            for (Sms sms : smses) {
                if (sms.receivedDate > maxReadTimeInMillis && sms.read) {
                    maxReadTimeInMillis = sms.receivedDate;
                    lastReadSms = sms;
                }
                if (sms.receivedDate > maxUnreadTimeInMillis && !sms.read) {
                    maxUnreadTimeInMillis = sms.receivedDate;
                    lastUnreadSms = sms;
                }
            }

            if (lastUnreadSms == null) {
                return new SerialSms(lastReadSms);
            }

            return new SerialSms(lastUnreadSms);
        }
        return null;
    }

    public void init(final Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = mInflater.inflate(R.layout.message_view, this, true);

        source = (TextView) rootView.findViewById(R.id.source);
        message = (TextView) rootView.findViewById(R.id.message);
        dateTime = (TextView) rootView.findViewById(R.id.date_time);

    }

    public void getData(Context context) {
        sms = getLastUnreadSms(context);
    }

    public void displaySms(final Context context) {
        try {
            source.setText(sms.address);
            message.setText(sms.body);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(sms.receivedDate);
            SimpleDateFormat dateFormat = TimeUtilities.getFullTimeFormat(context);
            String dateAndTime = dateFormat.format(calendar.getTime());

            dateTime.setText(dateAndTime);

            rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent defineIntent = new Intent(Intent.ACTION_VIEW);
                    defineIntent.setData(Uri.parse("content://mms-sms/conversations/" + sms.threadId));
                    context.startActivity(defineIntent);
                }
            });

        } catch (Exception e) {
            Log.e("CalendarView", e.getMessage(), e);
            setVisibility(View.GONE);
        }
    }
}

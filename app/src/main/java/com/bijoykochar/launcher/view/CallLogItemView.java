package com.bijoykochar.launcher.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SerialCall;
import com.bijoykochar.launcher.items.SerialContacts;

import java.util.Map;

import me.everything.providers.android.calllog.Call;

/**
 * Created by bijoy on 10/18/15.
 */
public class CallLogItemView extends LinearLayout {

    public ImageView profile, callType;
    public TextView callCount;
    public TextView title;

    public CallLogItemView(Context context) {
        super(context);
    }

    public CallLogItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CallLogItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CallLogItemView init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.call_log_item, this, true);

        profile = (ImageView) rootView.findViewById(R.id.profile_image);
        callCount = (TextView) rootView.findViewById(R.id.profile_count);
        callType = (ImageView) rootView.findViewById(R.id.call_type);
        title = (TextView) rootView.findViewById(R.id.title);

        return this;
    }

    public void displayCallLog(final Context context, final SerialCall log, Map<String, SerialContacts> contacts) {
        if (log.name == null || log.name.isEmpty()) {
            title.setText(log.number);
        } else {
            title.setText(log.name);
        }

        if (log.number != null && contacts.containsKey(log.number)) {
            if (contacts.get(log.number) != null && contacts.get(log.number).uriPhoto != null) {
                profile.setImageURI(Uri.parse(contacts.get(log.number).uriPhoto));
            }
        }

        callCount.setText(log.callCount.toString());
        if (log.type != null) {
            if (log.type.equals(Call.CallType.INCOMING)) {
                callType.setImageResource(R.drawable.ic_call_received_white_24dp);
                callType.setBackgroundColor(Color.parseColor("#AAFF9800"));
            } else if (log.type.equals(Call.CallType.MISSED)) {
                callType.setImageResource(R.drawable.ic_call_missed_white_24dp);
                callType.setBackgroundColor(Color.parseColor("#AAD50000"));
            } else if (log.type.equals(Call.CallType.OUTGOING)) {
                callType.setImageResource(R.drawable.ic_call_made_white_24dp);
                callType.setBackgroundColor(Color.parseColor("#AA388E3C"));
            } else {
                callType.setImageResource(R.drawable.ic_call_made_white_24dp);
                callType.setBackgroundColor(Color.parseColor("#AADD2C00"));
            }
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + log.number));
                context.startActivity(intent);
            }
        });

    }

    public void show() {
        profile.setVisibility(View.VISIBLE);
        callType.setVisibility(View.VISIBLE);
        callCount.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
    }

    public void hide() {
        profile.setVisibility(View.GONE);
        callType.setVisibility(View.GONE);
        callCount.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
    }
}

package com.bijoykochar.launcher.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bijoykochar.launcher.R;
import com.bijoykochar.launcher.items.SerialCall;
import com.bijoykochar.launcher.items.SerialContacts;
import com.bijoykochar.launcher.util.Utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.everything.providers.android.calllog.Call;
import me.everything.providers.android.calllog.CallsProvider;
import me.everything.providers.android.contacts.Contact;
import me.everything.providers.android.contacts.ContactsProvider;

/**
 * Created by bijoy on 10/18/15.
 */
public class CallLogsView extends LinearLayout {

    public Integer layerCount = 3;
    public LinearLayout[] layers = new LinearLayout[2];

    private List<SerialCall> recentCalls;
    private Map<String, SerialContacts> contacts;

    public CallLogsView(Context context) {
        super(context);
    }

    public CallLogsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CallLogsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public List<SerialCall> getCalls(Context context) {
        CallsProvider callsProvider = new CallsProvider(context);
        List<Call> calls = callsProvider.getCalls().getList();
        Collections.reverse(calls);

        List<SerialCall> filteredCalls = new ArrayList<>();
        Call lastCall = null;
        for (Call call : calls) {
            if (lastCall == null
                    || !(lastCall.number != null && call.number!= null && lastCall.number.equals(call.number)
                    && lastCall.type != null && call.type!= null && lastCall.type.equals(call.type))) {
                SerialCall serialCall = new SerialCall(call);
                serialCall.callCount = 1;
                filteredCalls.add(serialCall);
            } else {
                SerialCall lastSerialCall = filteredCalls.get(filteredCalls.size() - 1);
                lastSerialCall.callCount = lastSerialCall.callCount + 1;
                filteredCalls.set(filteredCalls.size() - 1, lastSerialCall);
            }
            lastCall = call;
        }

        return filteredCalls;
    }

    public Map<String, SerialContacts> getContacts(Context context) {
        ContactsProvider contactsProvider = new ContactsProvider(context);
        List<Contact> contacts = contactsProvider.getContacts().getList();

        Map<String, SerialContacts> mapping = new HashMap<>();
        for (Contact contact : contacts) {
            mapping.put(contact.phone, new SerialContacts(contact));
            mapping.put(contact.normilizedPhone, new SerialContacts(contact));
        }

        return mapping;
    }

    public CallLogsView init(Context context) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.call_log_view, this, true);

        layers[0] = (LinearLayout) rootView.findViewById(R.id.layer_1);
        layers[1] = (LinearLayout) rootView.findViewById(R.id.layer_2);

        return this;
    }

    public void getData(Context context) {
        recentCalls = getCalls(context);
        contacts = getContacts(context);

        double screenSize = Utilities.getScreenSizeInInches((Activity) context);
        if (screenSize < 6.0) {
            layerCount = 3;
        } else {
            layerCount = 5;
        }
    }

    public void displayCalls(Context context) {
        for (LinearLayout layout : layers) {
            layout.removeAllViews();
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        for (int i = 0; i < 2*layerCount; i++) {
            int layerId = 0;
            if (i >= layerCount) {
                layerId = 1;
            }

            CallLogItemView callView = (new CallLogItemView(context)).init(context);
            callView.setLayoutParams(params);
            callView.displayCallLog(context, recentCalls.get(i), contacts);

            layers[layerId].addView(callView);
        }
    }

}

package com.bijoykochar.launcher.items;

import java.io.Serializable;

import me.everything.providers.android.telephony.Sms;

/**
 * Created by bijoy on 10/19/15.
 */
public class SerialSms implements Serializable {
    public String address;
    public String body;
    public long receivedDate;
    public boolean seen;
    public int threadId;

    public SerialSms(Sms sms) {
        address = sms.address;
        body = sms.body;
        receivedDate = sms.receivedDate;
        seen = sms.seen;
        threadId = sms.threadId;
    }
}

package com.bijoykochar.launcher.items;

import java.io.Serializable;

import me.everything.providers.android.calllog.Call;

/**
 * The call count besides the call information
 * Created by bijoy on 10/18/15.
 */
public class SerialCall implements Serializable {
    public String name;
    public String number;
    public long id;
    public long duration;
    public boolean isRead;
    public Integer callCount;
    public Call.CallType type;

    public SerialCall(Call call) {
        this.name = call.name;
        this.number = call.number;
        this.type = call.type;
        this.id = call.id;
        this.duration = call.duration;
        this.isRead = call.isRead;
        this.callCount = 0;
    }
}

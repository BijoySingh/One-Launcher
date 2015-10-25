package com.bijoykochar.launcher.items;

import java.io.Serializable;

import me.everything.providers.android.contacts.Contact;

/**
 * Created by bijoy on 10/19/15.
 */
public class SerialContacts implements Serializable {
    public String uriPhoto;
    public String phone;
    public String displayName;

    public SerialContacts(Contact contact) {
        uriPhoto = contact.uriPhoto;
        phone = contact.phone;
        displayName = contact.displayName;
    }
}

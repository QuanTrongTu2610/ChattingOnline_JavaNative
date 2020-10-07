package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Plugins.Interface.ConvertObjectToHashMap;


import java.util.HashMap;

public class ConvertContactToHashMap implements ConvertObjectToHashMap<Contact> {
    private static ConvertContactToHashMap instance;

    public static ConvertContactToHashMap getInstance() {
        if (instance == null) {
            instance = new ConvertContactToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(Contact object) {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("contactId", object.getContactId());
        hash.put("contactUserId", object.getContactUserId());
        hash.put("connectedUserId", object.getConnectedUserId());
        return hash;
    }
}

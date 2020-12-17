package com.example.chattingonlineapplication.Utils;

import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Utils.Interface.IConvertObjectToHashMap;


import java.util.HashMap;

public class IConvertContactToHashMap implements IConvertObjectToHashMap<Contact> {
    private static IConvertContactToHashMap instance;

    public static IConvertContactToHashMap getInstance() {
        if (instance == null) {
            instance = new IConvertContactToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(Contact object) {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("contactId", object.getContactId());
        hash.put("conversationId", object.getConversationId());
        hash.put("participants", object.getParticipants());
        return hash;
    }
}

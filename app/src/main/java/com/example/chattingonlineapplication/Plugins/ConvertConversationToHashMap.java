package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.Conversation;

import java.util.HashMap;

public class ConvertConversationToHashMap implements ConvertObjectToHashMap<Conversation> {

    private static ConvertConversationToHashMap instance;

    public static ConvertConversationToHashMap getInstance() {
        if (instance == null) {
            instance = new ConvertConversationToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(Conversation object) {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("conversationId", object.getConversationId());
        hash.put("conversationLastActive", object.getConversationLastActive());
        hash.put("userId", object.getUserId());
        hash.put("userId2", object.getUserId2());
        return hash;
    }
}

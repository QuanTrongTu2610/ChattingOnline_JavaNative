package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Plugins.Interface.ConvertObjectToHashMap;

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
        hash.put("conversationTitle", object.getConversationTitle());
        hash.put("participants", object.getParticipants());
        hash.put("messages", object.getMessages());
        return hash;
    }
}

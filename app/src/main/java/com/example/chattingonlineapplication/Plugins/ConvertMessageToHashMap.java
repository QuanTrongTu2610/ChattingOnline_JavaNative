package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.Message;

import java.util.HashMap;

public class ConvertMessageToHashMap implements ConvertObjectToHashMap<Message> {
    private static ConvertMessageToHashMap instance;

    public static ConvertMessageToHashMap getInstance() {
        if (instance == null) {
            instance = new ConvertMessageToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(Message object) {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("messageId", object.getMessageId());
        hash.put("messageFromUser", object.getMessageFromUser());
        hash.put("messageContent", object.getMessageContent());
        hash.put("messageDateCreated", object.getMessageDateCreated());
        hash.put("messageSeenAt", object.getMessageSeenAt());
        hash.put("conversationId", object.getConversationId());
        return hash;
    }
}

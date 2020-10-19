package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Plugins.Interface.IConvertObjectToHashMap;

import java.util.HashMap;

public class IConvertMessageToHashMap implements IConvertObjectToHashMap<Message> {
    private static IConvertMessageToHashMap instance;

    public static IConvertMessageToHashMap getInstance() {
        if (instance == null) {
            instance = new IConvertMessageToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(Message object) {
//        HashMap<String, Object> hash = new HashMap<>();
//        hash.put("messageId", object.getMessageId());
//        hash.put("messageFromUser", object.getMessageFromUser());
//        hash.put("messageContent", object.getMessageContent());
//        hash.put("messageDateCreated", object.getMessageDateCreated());
//        hash.put("messageSeenAt", object.getMessageSeenAt());
//        hash.put("conversationId", object.getConversationId());
//        return hash;
        return null;
    }
}

package com.example.chattingonlineapplication.Utils;

import com.example.chattingonlineapplication.Models.GroupChat;
import com.example.chattingonlineapplication.Utils.Interface.IConvertObjectToHashMap;

import java.util.HashMap;

public class IConvertGroupToHashMap implements IConvertObjectToHashMap<GroupChat> {

    private static IConvertObjectToHashMap instance;

    public static IConvertObjectToHashMap getInstance() {
        if (instance == null) {
            instance = new IConvertGroupToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(GroupChat object) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("groupId", object.getGroupId());
        hashMap.put("groupTitle", object.getGroupTitle());
        hashMap.put("groupAuthorId", object.getGroupAuthorId());
        hashMap.put("groupAvatarUrl", object.getGroupAvatarUrl());
        hashMap.put("participants", object.getParticipants());
        return hashMap;
    }
}

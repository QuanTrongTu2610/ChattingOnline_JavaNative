package com.example.chattingonlineapplication.Utils;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Utils.Interface.IConvertObjectToHashMap;

import java.util.HashMap;

public class IConvertUserToHashMap implements IConvertObjectToHashMap<User> {
    private static IConvertUserToHashMap instance;

    public static IConvertUserToHashMap getInstance() {
        if (instance == null) {
            instance = new IConvertUserToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(User object) {
        try {
            HashMap<String, Object> container = new HashMap<>();
            container.put("userId", object.getUserId());
            container.put("userFirstName", object.getUserFirstName());
            container.put("userLastName", object.getUserLastName());
            container.put("userBio", object.getUserBio());
            container.put("userPhoneNumber", object.getUserPhoneNumber());
            container.put("userAvatarUrl", object.getUserAvatarUrl());
            container.put("userDateUpdated", object.getUserDateUpdated());
            container.put("userDateCreated", object.getUserDateCreated());
            container.put("userIsActive", object.isUserIsActive());
            container.put("userIpAddress", object.getUserIpAddress());
            container.put("userPort", object.getUserPort());
            return container;
        } catch (Exception e) {
            return null;
        }
    }
}

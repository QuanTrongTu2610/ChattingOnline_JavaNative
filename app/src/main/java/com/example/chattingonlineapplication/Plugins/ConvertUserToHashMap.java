package com.example.chattingonlineapplication.Plugins;

import com.example.chattingonlineapplication.Models.User;

import java.util.HashMap;

public class ConvertUserToHashMap implements ConvertObjectToHashMap<User> {
    private static ConvertUserToHashMap instance;

    public static ConvertUserToHashMap getInstance() {
        if (instance == null) {
            instance = new ConvertUserToHashMap();
        }
        return instance;
    }

    @Override
    public HashMap<String, Object> convert(User object) {
        try {
            HashMap<String, Object> container = new HashMap<>();
            container.put("userId", object.getUserId());
            container.put("userLastName", object.getUserLastName());
            container.put("userBio", object.getUserBio());
            container.put("userPhoneNumber", object.getUserPhoneNumber());
            container.put("userAvatarUrl", object.getUserAvatarUrl());
            container.put("userDateUpdated", object.getUserDateUpdated());
            container.put("userDateCreated", object.getUserDateCreated());
            container.put("userIsActive", object.isUserIsActive());
            container.put("userIpAddress", object.getUserIPAddress());
            container.put("userPort", object.getUserPort());
            return container;
        } catch (Exception e) {
            return null;
        }
    }
}

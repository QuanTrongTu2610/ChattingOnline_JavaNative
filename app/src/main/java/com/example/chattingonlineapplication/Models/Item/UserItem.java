package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.User;

public class UserItem extends User {
    public UserItem(String userId, String userFirstName, String userLastName, String userPhoneNumber, String userAvatarUrl, String userBio, long userDateUpdated, long userDateCreated, boolean userIsActive, String userIPAddress, int userPort) {
        super(userId, userFirstName, userLastName, userPhoneNumber, userAvatarUrl, userBio, userDateUpdated, userDateCreated, userIsActive, userIPAddress, userPort);
    }
}

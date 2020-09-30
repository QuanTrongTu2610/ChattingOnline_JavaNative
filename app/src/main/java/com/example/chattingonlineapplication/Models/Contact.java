package com.example.chattingonlineapplication.Models;

public class Contact {
    private String contactId;
    private String userId;
    private User userFriend;

    public Contact() {
    }

    public Contact(String contactId, String userId, User userFriend) {
        this.contactId = contactId;
        this.userId = userId;
        this.userFriend = userFriend;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(User userFriend) {
        this.userFriend = userFriend;
    }
}

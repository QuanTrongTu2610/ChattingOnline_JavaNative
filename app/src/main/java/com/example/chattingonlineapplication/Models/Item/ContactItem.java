package com.example.chattingonlineapplication.Models.Item;

public class ContactItem {
    private String userAvatarUrl;
    private String userName;
    private long lastMessageAt;

    public ContactItem() {}
    public ContactItem(String userAvatarUrl, String userName, long lastMessageAt) {
        this.userAvatarUrl = userAvatarUrl;
        this.userName = userName;
        this.lastMessageAt = lastMessageAt;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(long lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}

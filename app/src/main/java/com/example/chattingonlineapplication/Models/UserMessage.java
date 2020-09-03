package com.example.chattingonlineapplication.Models;

import java.util.Date;

public class UserMessage {
    private User user;
    private Message userCurrentMessage;
    private Message userPreviousMessage;
    private String userTimeSend;
    private boolean isUserSeen;

    public UserMessage(User user, Message userCurrentMessage, Message userPreviousMessage, String userTimeSend, boolean isUserSeen) {
        this.user = user;
        this.userCurrentMessage = userCurrentMessage;
        this.userPreviousMessage = userPreviousMessage;
        this.userTimeSend = userTimeSend;
        this.isUserSeen = isUserSeen;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getUserCurrentMessage() {
        return userCurrentMessage;
    }

    public void setUserCurrentMessage(Message userCurrentMessage) {
        this.userCurrentMessage = userCurrentMessage;
    }

    public Message getUserPreviousMessage() {
        return userPreviousMessage;
    }

    public void setUserPreviousMessage(Message userPreviousMessage) {
        this.userPreviousMessage = userPreviousMessage;
    }

    public String getUserTimeSend() {
        return userTimeSend;
    }

    public void setUserTimeSend(String userTimeSend) {
        this.userTimeSend = userTimeSend;
    }

    public boolean isUserSeen() {
        return isUserSeen;
    }

    public void setUserSeen(boolean userSeen) {
        isUserSeen = userSeen;
    }
}

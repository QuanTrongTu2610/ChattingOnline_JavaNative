package com.example.chattingonlineapplication.Models;

public class Message {
    private String messageId;
    private String userSenderId;
    private String userReceiverId;
    private String content;
    private long messageDateCreated;
    private long messageSeenAt;

    public Message() {}

    public Message(String messageId, String userSenderId, String userReceiverId, String content, long messageDateCreated, long messageSeenAt) {
        this.messageId = messageId;
        this.userSenderId = userSenderId;
        this.userReceiverId = userReceiverId;
        this.content = content;
        this.messageDateCreated = messageDateCreated;
        this.messageSeenAt = messageSeenAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(String userSenderId) {
        this.userSenderId = userSenderId;
    }

    public String getUserReceiverId() {
        return userReceiverId;
    }

    public void setUserReceiverId(String userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMessageDateCreated() {
        return messageDateCreated;
    }

    public void setMessageDateCreated(long messageDateCreated) {
        this.messageDateCreated = messageDateCreated;
    }

    public long getMessageSeenAt() {
        return messageSeenAt;
    }

    public void setMessageSeenAt(long messageSeenAt) {
        this.messageSeenAt = messageSeenAt;
    }
}

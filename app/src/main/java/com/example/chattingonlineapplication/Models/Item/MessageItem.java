package com.example.chattingonlineapplication.Models.Item;

public class MessageItem {
    private String messageUserName;
    private String messageUserUrl;
    private String mesageId;
    private String messageFromUser;
    private String messageContent;
    private long messageDateCreated;
    private long messageSeenAt;
    private String conversationId;

    public MessageItem(String messageUserName, String messageUserUrl, String mesageId, String messageFromUser, String messageContent, long messageDateCreated, long messageSeenAt, String conversationId) {
        this.messageUserName = messageUserName;
        this.messageUserUrl = messageUserUrl;
        this.mesageId = mesageId;
        this.messageFromUser = messageFromUser;
        this.messageContent = messageContent;
        this.messageDateCreated = messageDateCreated;
        this.messageSeenAt = messageSeenAt;
        this.conversationId = conversationId;
    }

    public String getMessageUserUrl() {
        return messageUserUrl;
    }

    public void setMessageUserUrl(String messageUserUrl) {
        this.messageUserUrl = messageUserUrl;
    }

    public String getMesageId() {
        return mesageId;
    }

    public void setMesageId(String mesageId) {
        this.mesageId = mesageId;
    }

    public String getMessageFromUser() {
        return messageFromUser;
    }

    public void setMessageFromUser(String messageFromUser) {
        this.messageFromUser = messageFromUser;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessageUserName() {
        return messageUserName;
    }

    public void setMessageUserName(String messageUserName) {
        this.messageUserName = messageUserName;
    }
}


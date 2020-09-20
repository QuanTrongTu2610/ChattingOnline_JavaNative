package com.example.chattingonlineapplication.Models;

public class Message {
    private String messageId;
    private String messageFromUser;
    private String messageContent;
    private long messageDateCreated;
    private long messageSeenAt;
    private String conversationId;

    public Message(String messageId, String messageFromUser, String messageContent, long messageDateCreated, long messageSeenAt, String conversationId) {
        this.messageId = messageId;
        this.messageFromUser = messageFromUser;
        this.messageContent = messageContent;
        this.messageDateCreated = messageDateCreated;
        this.messageSeenAt = messageSeenAt;
        this.conversationId = conversationId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String mesageId) {
        this.messageId = mesageId;
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
}

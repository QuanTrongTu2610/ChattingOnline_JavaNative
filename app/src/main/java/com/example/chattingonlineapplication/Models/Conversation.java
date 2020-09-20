package com.example.chattingonlineapplication.Models;

public class Conversation {
    private String conversationId;
    private long conversationLastActive;
    private String userId;
    private String userId2;

    public Conversation(String conversationId, long conversationLastActive, String userId, String userId2) {
        this.conversationId = conversationId;
        this.conversationLastActive = conversationLastActive;
        this.userId = userId;
        this.userId2 = userId2;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public long getConversationLastActive() {
        return conversationLastActive;
    }

    public void conversationLastActive(long conversationLastActive) {
        this.conversationLastActive = conversationLastActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }
}

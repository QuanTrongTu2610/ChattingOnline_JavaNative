package com.example.chattingonlineapplication.Models;

public class Conversation {
    private String conversationId;
    private String user1;
    private String user2;
    private Message lastMessage;

    public Conversation() {}

    public Conversation(String conversationId, String user1, String user2, Message lastMessage) {
        this.conversationId = conversationId;
        this.user1 = user1;
        this.user2 = user2;
        this.lastMessage = lastMessage;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public Message getLastMessage() {
        return lastMessage;
    }
}

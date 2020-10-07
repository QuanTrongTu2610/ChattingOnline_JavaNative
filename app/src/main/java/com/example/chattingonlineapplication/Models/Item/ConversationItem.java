package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;

public class ConversationItem {
    private String conversationId;
    private User user1;
    private User user2;
    private Message lastMessage;

    public ConversationItem(String conversationId, User user1, User user2, Message lastMessage) {
        this.conversationId = conversationId;
        this.user1 = user1;
        this.user2 = user2;
        this.lastMessage = lastMessage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}

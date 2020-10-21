package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;

public class ConversationItem {
    private String conversationId;
    private User contactUser;
    private User connectedUser;
    private Message lastMessage;

    public ConversationItem(String conversationId, User contactUser, User connectedUser, Message lastMessage) {
        this.conversationId = conversationId;
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
        this.lastMessage = lastMessage;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public User getContactUser() {
        return contactUser;
    }

    public void setContactUser(User contactUser) {
        this.contactUser = contactUser;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}

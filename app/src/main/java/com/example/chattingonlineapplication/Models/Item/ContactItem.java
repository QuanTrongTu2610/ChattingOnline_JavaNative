package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.User;

public class ContactItem {
    private String contactId;
    private User owner;
    private User connectedUser;
    private String conversationId;

    public ContactItem(String contactId, User owner, User connectedUser, String conversationId) {
        this.contactId = contactId;
        this.owner = owner;
        this.connectedUser = connectedUser;
        this.conversationId = conversationId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(User connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}

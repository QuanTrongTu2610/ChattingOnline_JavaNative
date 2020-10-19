package com.example.chattingonlineapplication.Models;

public class Contact {
    private String contactId;
    private String contactUserId;
    private String connectedUserId;
    private String conversationId;

    public Contact() {
    }

    public Contact(String contactId, String contactUserId, String connectedUserId, String conversationId) {
        this.contactId = contactId;
        this.contactUserId = contactUserId;
        this.connectedUserId = connectedUserId;
        this.conversationId = conversationId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    public String getConnectedUserId() {
        return connectedUserId;
    }

    public void setConnectedUserId(String connectedUserId) {
        this.connectedUserId = connectedUserId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}

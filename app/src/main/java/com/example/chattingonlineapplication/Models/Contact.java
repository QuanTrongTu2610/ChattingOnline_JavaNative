package com.example.chattingonlineapplication.Models;

public class Contact {
    private String contactId;
    private String contactUserId;
    private String connectedUserId;

    public Contact() {
    }

    public Contact(String contactId, String contactUserId, String connectedUserId) {
        this.contactId = contactId;
        this.contactUserId = contactUserId;
        this.connectedUserId = connectedUserId;
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
}

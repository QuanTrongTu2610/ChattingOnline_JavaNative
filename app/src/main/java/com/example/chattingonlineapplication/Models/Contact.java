package com.example.chattingonlineapplication.Models;

public class Contact {
    private String contactId;
    private User contactUser;
    private User connectedUser;

    public Contact() {
    }

    public Contact(String contactId, User contactUser, User connectedUser) {
        this.contactId = contactId;
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
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
}

package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.User;

public class ContactItem {
    private String contactId;
    private User contactUser;
    private User connectedUser;

    public ContactItem(String contactId, User contactUser, User connectedUser) {
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

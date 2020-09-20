package com.example.chattingonlineapplication.Models;

public class Contact {
    private String contactId;
    private long contactLastActive;
    private String userId;
    private String userId2;

    public Contact(String contactId, long contactLastActive, String userId, String userId2) {
        this.contactId = contactId;
        this.contactLastActive = contactLastActive;
        this.userId = userId;
        this.userId2 = userId2;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public long getContactLastActive() {
        return contactLastActive;
    }

    public void setContactLastActive(long contactLastActive) {
        this.contactLastActive = contactLastActive;
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

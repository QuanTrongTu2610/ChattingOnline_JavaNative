package com.example.chattingonlineapplication.Models;

import java.util.ArrayList;

public class Contact {
    private String contactId;
    private String conversationId;
    private ArrayList<String> participants;

    public Contact() {
    }

    public Contact(String contactId, String conversationId, ArrayList<String> participants) {
        this.contactId = contactId;
        this.conversationId = conversationId;
        this.participants = participants;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }
}

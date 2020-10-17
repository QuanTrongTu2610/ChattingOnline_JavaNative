package com.example.chattingonlineapplication.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private String conversationId;
    private String conversationTitle;
    private ArrayList<String> participants;
    private Map<String,Message> messages;

    public Conversation() {}

    public Conversation(String conversationId, String conversationTitle, ArrayList<String> participants, Map<String, Message> messages) {
        this.conversationId = conversationId;
        this.conversationTitle = conversationTitle;
        this.participants = participants;
        this.messages = messages;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public Map<String, Message> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, Message> messages) {
        this.messages = messages;
    }
}

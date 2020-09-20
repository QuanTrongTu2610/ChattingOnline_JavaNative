package com.example.chattingonlineapplication.Models.Item;

public class ConversationItem {
    private int cId;
    private int cReceiverId;
    private String cLastMessage;
    private String cDateSending;
    private String cReceiverName;
    private String cReceiverAvatarUrl;

    public ConversationItem(int cId, int cReceiverId, String cLastMessage, String cDateSending, String cReceiverName, String cReceiverAvatarUrl) {
        this.cId = cId;
        this.cReceiverId = cReceiverId;
        this.cLastMessage = cLastMessage;
        this.cDateSending = cDateSending;
        this.cReceiverName = cReceiverName;
        this.cReceiverAvatarUrl = cReceiverAvatarUrl;
    }

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getcReceiverId() {
        return cReceiverId;
    }

    public void setcReceiverId(int cReceiverId) {
        this.cReceiverId = cReceiverId;
    }

    public String getcLastMessage() {
        return cLastMessage;
    }

    public void setcLastMessage(String cLastMessage) {
        this.cLastMessage = cLastMessage;
    }

    public String getcDateSending() {
        return cDateSending;
    }

    public void setcDateSending(String cDateSending) {
        this.cDateSending = cDateSending;
    }

    public String getcReceiverName() {
        return cReceiverName;
    }

    public void setcReceiverName(String cReceiverName) {
        this.cReceiverName = cReceiverName;
    }

    public String getcReceiverAvatarUrl() {
        return cReceiverAvatarUrl;
    }

    public void setcReceiverAvatarUrl(String cReceiverAvatarUrl) {
        this.cReceiverAvatarUrl = cReceiverAvatarUrl;
    }
}

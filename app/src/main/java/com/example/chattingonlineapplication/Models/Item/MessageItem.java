package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;

public class MessageItem {
    private String messageId;
    private User userSender;
    private User userReceiver;
    private String content;
    private long messageDateCreated;
    private long messageSeenAt;
    private String conversationId;

    public MessageItem() {}

    public MessageItem(String messageId, User userSender, User userReceiver, String content, long messageDateCreated, long messageSeenAt, String conversationId) {
        this.messageId = messageId;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.content = content;
        this.messageDateCreated = messageDateCreated;
        this.messageSeenAt = messageSeenAt;
        this.conversationId = conversationId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public User getUserReceiver() {
        return userReceiver;
    }

    public void setUserReceiver(User userReceiver) {
        this.userReceiver = userReceiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMessageDateCreated() {
        return messageDateCreated;
    }

    public void setMessageDateCreated(long messageDateCreated) {
        this.messageDateCreated = messageDateCreated;
    }

    public long getMessageSeenAt() {
        return messageSeenAt;
    }

    public void setMessageSeenAt(long messageSeenAt) {
        this.messageSeenAt = messageSeenAt;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}

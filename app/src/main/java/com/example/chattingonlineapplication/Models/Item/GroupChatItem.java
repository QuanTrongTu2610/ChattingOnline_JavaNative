package com.example.chattingonlineapplication.Models.Item;

import com.example.chattingonlineapplication.Models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class GroupChatItem implements Serializable {
    private String groupId;
    private String title;
    private String avatarUrl;
    private User author;
    private ArrayList<User> participant;

    public GroupChatItem() {

    }

    public GroupChatItem(String groupId, String title, String avatarUrl, User author, ArrayList<User> participant) {
        this.groupId = groupId;
        this.title = title;
        this.avatarUrl = avatarUrl;
        this.author = author;
        this.participant = participant;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public ArrayList<User> getParticipant() {
        return participant;
    }

    public void setParticipant(ArrayList<User> participant) {
        this.participant = participant;
    }
}

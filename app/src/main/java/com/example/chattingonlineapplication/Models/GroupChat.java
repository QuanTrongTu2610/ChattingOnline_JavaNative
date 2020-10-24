package com.example.chattingonlineapplication.Models;

import java.util.ArrayList;

public class GroupChat {

    private String groupId;
    private String groupTitle;
    private String groupAvatarUrl;
    private String groupAuthorId;
    private ArrayList<String> participants;

    public GroupChat() {}

    public GroupChat(String groupId, String groupTitle, String groupAvatarUrl, String groupAuthorId, ArrayList<String> participants) {
        this.groupId = groupId;
        this.groupTitle = groupTitle;
        this.groupAvatarUrl = groupAvatarUrl;
        this.groupAuthorId = groupAuthorId;
        this.participants = participants;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupAvatarUrl() {
        return groupAvatarUrl;
    }

    public void setGroupAvatarUrl(String groupAvatarUrl) {
        this.groupAvatarUrl = groupAvatarUrl;
    }

    public String getGroupAuthorId() {
        return groupAuthorId;
    }

    public void setGroupAuthorId(String groupAuthorId) {
        this.groupAuthorId = groupAuthorId;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }
}

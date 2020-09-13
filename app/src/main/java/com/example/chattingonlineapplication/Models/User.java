package com.example.chattingonlineapplication.Models;

import java.util.List;

public class User {
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userName;
    private String userPhoneNumber;
    private String userAvatarUrl;
    private String userBio;
    private String userDateUpdated;
    private String userDateCreated;
    private boolean userIsActive;

    public User(String userId, String userFirstName, String userLastName, String userName, String userPhoneNumber, String userAvatarUrl, String userBio, String userDateUpdated, String userDateCreated, boolean userIsActive) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userAvatarUrl = userAvatarUrl;
        this.userBio = userBio;
        this.userDateUpdated = userDateUpdated;
        this.userDateCreated = userDateCreated;
        this.userIsActive = userIsActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserDateUpdated() {
        return userDateUpdated;
    }

    public void setUserDateUpdated(String userDateUpdated) {
        this.userDateUpdated = userDateUpdated;
    }

    public boolean getUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(boolean userIsActive) {
        this.userIsActive = userIsActive;
    }

    public String getUserDateCreated() {
        return userDateCreated;
    }

    public void setUserDateCreated(String userDateCreated) {
        this.userDateCreated = userDateCreated;
    }
}


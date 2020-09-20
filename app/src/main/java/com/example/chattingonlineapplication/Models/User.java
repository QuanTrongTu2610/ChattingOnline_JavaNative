package com.example.chattingonlineapplication.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userPhoneNumber;
    private String userAvatarUrl;
    private String userBio;
    private long userDateUpdated;
    private long userDateCreated;
    private boolean userIsActive;
    private String userIPAddress;
    private int userPort;

    public User() {

    }

    public User(String userId, String userFirstName, String userLastName, String userPhoneNumber, String userAvatarUrl, String userBio, long userDateUpdated, long userDateCreated, boolean userIsActive, String userIPAddress, int userPort) {
        this.userId = userId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userPhoneNumber = userPhoneNumber;
        this.userAvatarUrl = userAvatarUrl;
        this.userBio = userBio;
        this.userDateUpdated = userDateUpdated;
        this.userDateCreated = userDateCreated;
        this.userIsActive = userIsActive;
        this.userIPAddress = userIPAddress;
        this.userPort = userPort;
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

    public long getUserDateUpdated() {
        return userDateUpdated;
    }

    public void setUserDateUpdated(long userDateUpdated) {
        this.userDateUpdated = userDateUpdated;
    }

    public long getUserDateCreated() {
        return userDateCreated;
    }

    public void setUserDateCreated(long userDateCreated) {
        this.userDateCreated = userDateCreated;
    }

    public boolean isUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(boolean userIsActive) {
        this.userIsActive = userIsActive;
    }

    public String getUserIPAddress() {
        return userIPAddress;
    }

    public void setUserIPAddress(String userIPAddress) {
        this.userIPAddress = userIPAddress;
    }

    public int getUserPort() {
        return userPort;
    }

    public void setUserPort(int userPort) {
        this.userPort = userPort;
    }
}




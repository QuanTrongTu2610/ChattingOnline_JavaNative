package com.example.chattingonlineapplication.Models;

import java.util.List;

public class User {
    private int userId;
    private String userName;
    private String userPassword;
    private String userPhoneNumber;
    private String userAvatarUrl;
    private String userBio;
    private List<User> userFriends;

    public User(int userId, String userName, String userPassword, String userPhoneNumber, String userAvatarUrl, String userBio, List<User> userFriends) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPhoneNumber = userPhoneNumber;
        this.userAvatarUrl = userAvatarUrl;
        this.userBio = userBio;
        this.userFriends = userFriends;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
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

    public List<User> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<User> userFriends) {
        this.userFriends = userFriends;
    }
}

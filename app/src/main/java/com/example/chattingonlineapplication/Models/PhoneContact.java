package com.example.chattingonlineapplication.Models;

public class PhoneContact {
    private int userNumber;
    private String userName;
    private String phoneNumber;

    public PhoneContact(int userNumber, String userName, String phoneNumber) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public PhoneContact() {
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

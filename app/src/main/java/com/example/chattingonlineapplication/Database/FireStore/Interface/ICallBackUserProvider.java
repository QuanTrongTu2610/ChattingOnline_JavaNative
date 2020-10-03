package com.example.chattingonlineapplication.Database.FireStore.Interface;

import com.example.chattingonlineapplication.Models.User;

public interface ICallBackUserProvider {
    void createClient(User user1, User user2);
}

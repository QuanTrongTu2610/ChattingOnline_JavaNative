package com.example.chattingonlineapplication.Plugins.Interface;

import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;

import java.util.List;

public interface IUpDateChatViewRecycler {
    void updateItem(String message);
}

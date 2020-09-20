package com.example.chattingonlineapplication.Socket;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Item.MessageItem;

import java.util.ArrayList;

public class Client extends Thread {


    private String ownIP;
    private int socketPort;
    private Context context;
    private Activity activity;
    private RecyclerView messageView;
    private ArrayList<MessageItem> lstMessage;
    private ListMessageAdapter adapter;



    @Override
    public void run() {
        super.run();
    }
}

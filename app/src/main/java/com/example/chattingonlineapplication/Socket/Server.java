package com.example.chattingonlineapplication.Socket;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;


import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

//ServerToReceive message
public class Server extends Thread {

    private User contactUser;
    private User connectedUser;
    private List<MessageItem> listMessageItems;
    private ListMessageAdapter listMessageAdapter;
    private RecyclerView recyclerView;

    public Server(User contactUser, User connectedUser, List<MessageItem> listMessageItems, ListMessageAdapter listMessageAdapter, RecyclerView recyclerView) {
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
        this.listMessageItems = listMessageItems;
        this.listMessageAdapter = listMessageAdapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(contactUser.getUserPort());
            Log.i("ServerSocket", "Server is staring at IP: " + contactUser.getUserIpAddress() + " port: " + contactUser.getUserPort());
            while (!Thread.interrupted()) {
                Socket connection = serverSocket.accept();
                //callReceiveMessage
                ReceiveMessage receiveMessage = new ReceiveMessage();
                receiveMessage.execute(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class ReceiveMessage extends AsyncTask<Socket, Void, String> {
        String text;

        @Override
        protected String doInBackground(Socket... sockets) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
                text = br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            //show Message
            //return message when server get
            listMessageItems.add(new MessageItem("1", connectedUser, contactUser, s, (new Timestamp(System.currentTimeMillis())).getTime(), 0, "1"));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}

package com.example.chattingonlineapplication.Socket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Item.MessageItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//ServerToReceive message
public class Server extends Thread {

    private String serverIpAddress;
    private int socketPort;
    private Context context;
    private Activity activity;
    private RecyclerView messageView;
    private ArrayList<MessageItem> lstMessage;
    private ListMessageAdapter adapter;

    public Server(String serverIpAddress, int socketPort, Context context, Activity activity, RecyclerView messageView, ArrayList<MessageItem> lstMessage, ListMessageAdapter adapter) {
        this.serverIpAddress = serverIpAddress;
        this.socketPort = socketPort;
        this.context = context;
        this.activity = activity;
        this.messageView = messageView;
        this.lstMessage = lstMessage;
        this.adapter = adapter;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(socketPort);
            Log.i("ServerSocket", "Server is staring at IP: " + serverIpAddress + " port: " + socketPort);
            serverSocket.setReuseAddress(true);
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

        @Override
        protected String doInBackground(Socket... sockets) {
            String text = "";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
                text = br.readLine();
                Log.i("Message Receive", "Received => " + text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            //show Message
        }
    }
}

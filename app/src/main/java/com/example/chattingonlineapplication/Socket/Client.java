package com.example.chattingonlineapplication.Socket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Message;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends AsyncTask<Void, Void, String> {


    private String ownIP;
    private int friendPort;
    private Message message;

    private Context context;
    private Activity activity;
    private RecyclerView messageView;

    private ArrayList<Message> lstMessage;
    private ListMessageAdapter adapter;

    //
    private Socket client;
    private OutputStream outToServer;
    private PrintWriter output;


    public Client(String ownIP, int friendPort, Message message) throws Exception {
        this.message = message;
        this.ownIP = ownIP;
        this.friendPort = friendPort;
        //
        this.client = new Socket(ownIP, friendPort);
        this.outToServer = this.client.getOutputStream();
        this.output = new PrintWriter(this.outToServer);
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {

            //Intialize client side
            this.client = new Socket(ownIP, friendPort);
            this.outToServer = this.client.getOutputStream();
            this.output = new PrintWriter(this.outToServer);


            output.println(message.getContent());
            output.flush();
            this.client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message.getContent();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.i("Message", s);
        super.onPostExecute(s);
    }
}

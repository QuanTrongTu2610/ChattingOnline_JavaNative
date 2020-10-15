package com.example.chattingonlineapplication.Socket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Client extends AsyncTask<Void, Void, String> {
    private User contactUser;
    private User connectedUser;

    //More Variable
    private List<MessageItem> listMessageItems;
    private ListMessageAdapter listMessageAdapter;
    private RecyclerView recyclerView;
    private EditText EditText;

    public Client(User contactUser, User connectedUser, List<MessageItem> listMessageItems, ListMessageAdapter listMessageAdapter, RecyclerView recyclerView, EditText text) {
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
        this.listMessageItems = listMessageItems;
        this.listMessageAdapter = listMessageAdapter;
        this.recyclerView = recyclerView;
        this.EditText = text;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String message = EditText.getText().toString().trim();
        try {
//            Socket client = new Socket(connectedUser.getUserIpAddress(), connectedUser.getUserPort());
            Socket client = new Socket("172.20.10.13", 7800);

            Log.i("Client", "[ Client connect to " +
                    "Ip: " +
                    connectedUser.getUserIpAddress() +
                    "Port: " +
                    connectedUser.getUserPort() +
                    "]"
            );

            OutputStream outputStream = client.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);

            printWriter.println(message);
            printWriter.flush();
            outputStream.close();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listMessageItems.add(new MessageItem("1", contactUser, connectedUser, s, (new Timestamp(System.currentTimeMillis())).getTime(), 0, "1"));
        recyclerView.getAdapter().notifyDataSetChanged();
        EditText.setText("");
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }
}

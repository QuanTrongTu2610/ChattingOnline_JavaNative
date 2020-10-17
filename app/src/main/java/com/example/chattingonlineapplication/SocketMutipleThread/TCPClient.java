package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

public class TCPClient extends AsyncTask<Void, Void, String> {

    private User contactUser;
    private User connectedUser;
    private Socket socket;
    private String message;
    private List<MessageItem> listMessageItems;
    private RecyclerView recyclerView;
    private EditText editText;

    public TCPClient(User contactUser, User connectedUser, String message, List<MessageItem> listMessageItems, RecyclerView recyclerView, EditText editText) throws IOException {
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
        this.message = message;
        this.listMessageItems = listMessageItems;
        this.recyclerView = recyclerView;
        this.editText = editText;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            this.socket = new Socket("192.168.1.14", 8000);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(message);
            printWriter.flush();
            outputStream.close();
            socket.close();
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listMessageItems.add(
                new MessageItem("1",
                        contactUser,
                        connectedUser,
                        s,
                        (new Timestamp(System.currentTimeMillis())).getTime(),
                        0,
                        "1")
        );
        recyclerView.getAdapter().notifyDataSetChanged();
        editText.setText("");
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

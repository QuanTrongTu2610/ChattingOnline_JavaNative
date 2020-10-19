package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {
    private User connectedUser;
    private Socket socket;
    private SendingMessage clientThread;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;

    public TCPClient(User connectedUser) {
        Log.i("Client Creating...", connectedUser.getUserIpAddress());
        this.connectedUser = connectedUser;
    }

    public void registerUpdateChatViewRecyclerEvent(IUpDateChatViewRecycler i) {
        this.iUpDateChatViewRecycler = i;
    }

    public void sendingMessage(String message) {
        clientThread = new SendingMessage(message);
        clientThread.start();
    }

    public void onDestroy() {
        if (socket != null) {
            try {
                clientThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Thread Sending Message---------------------------------------------------------------------------
    public class SendingMessage extends Thread {
        private String message;
        public SendingMessage(String m) {
            this.message = m;
        }
        @Override
        public void run() {
            super.run();
            try {
                socket = new Socket(connectedUser.getUserIpAddress(), connectedUser.getUserPort());
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(message);
                printWriter.flush();
                outputStream.close();
                socket.close();
                if (iUpDateChatViewRecycler != null) {
                    iUpDateChatViewRecycler.updateItem(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

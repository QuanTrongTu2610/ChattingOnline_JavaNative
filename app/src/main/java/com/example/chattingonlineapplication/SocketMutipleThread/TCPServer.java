package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


//ServerToReceive message
public class TCPServer {

    private ClientRequestHandler clientRequestHandler;
    private Socket connection;
    private ServerSocket serverSocket;
    private boolean stop;
    private User contactUser;
    private Thread socketServerThread;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;

    public void registerUpdateChatViewRecyclerEvent(IUpDateChatViewRecycler i) {
        Log.i("Server Creating...", contactUser.getUserIpAddress());
        this.iUpDateChatViewRecycler = i;
    }

    public TCPServer(User contactUser) {
        this.contactUser = contactUser;
        socketServerThread = new SocketServerThread();
        socketServerThread.start();
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                stop = true;
                socketServerThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Separate Thread to run Server----------------------------------------------------------------------
    class SocketServerThread extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(contactUser.getUserPort());
                if (!serverSocket.isClosed()) {
                    while (stop == false) {
                        //callReceiveMessage
                        connection = serverSocket.accept();
                        Log.i("Connection", connection.toString());
                        //Async to handle each user's request.
                        if (connection != null && connection.isConnected()) {
                            clientRequestHandler = new ClientRequestHandler(connection, iUpDateChatViewRecycler);
                            clientRequestHandler.start();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;

import java.net.ServerSocket;
import java.net.Socket;

//ServerToReceive message
public class TCPServer {

    private Socket connection;
    private ServerSocket serverSocket;
    private boolean stop;
    private User owner;
    private Thread socketServerThread;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;

    public void registerUpdateChatViewRecyclerEvent(IUpDateChatViewRecycler i) {
        Log.i("Server Creating...", owner.getUserIpAddress());
        this.iUpDateChatViewRecycler = i;
    }

    public TCPServer(User owner) {
        this.owner = owner;
        socketServerThread = new SocketServerThread();
        socketServerThread.start();
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                stop = true;
                serverSocket.close();
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
                serverSocket = new ServerSocket(owner.getUserPort());
                if (!serverSocket.isClosed()) {
                    while (stop == false) {
                        //callReceiveMessage
                        connection = serverSocket.accept();
                        Log.i("Connection", connection.toString());
                        //Async to handle each user's request.
                        if (connection != null && connection.isConnected()) {
                            new ClientRequestHandler(connection, iUpDateChatViewRecycler).start();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


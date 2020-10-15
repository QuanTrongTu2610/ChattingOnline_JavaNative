package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Socket.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

//ServerToReceive message
public class TCPServer extends Thread {
    private static Vector<ClientRequestHandler> clRequests = new Vector<>();

    private ServerSocket serverSocket;

    public TCPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            while (!this.isInterrupted()) {
                //callReceiveMessage
                Socket connection = serverSocket.accept();
                Log.i("Connection", connection.toString());
                if (connection.isConnected()) {
                    ClientRequestHandler clientRequestHandler = new ClientRequestHandler(connection);
                    clRequests.add(clientRequestHandler);
                    clientRequestHandler.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdownServer() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

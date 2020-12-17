package com.example.chattingonlineapplication.SSLSocketMutipleThread;

import android.content.Context;
import android.util.Log;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.SocketMutipleThread.ClientRequestHandler;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.Utils.SSL.SSLProvider;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class TCPServerSSL {
    public SSLServerSocket sslServerSocket = null;
    private SSLSocket connection;
    private boolean stop;
    private User owner;
    private Thread socketServerThread;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;
    private final static char[] keystorepass = "123456789".toCharArray();
    private Context context;

    public void registerUpdateChatViewRecyclerEvent(IUpDateChatViewRecycler i) {
        Log.i("Server Creating...", owner.getUserIpAddress());
        this.iUpDateChatViewRecycler = i;
    }

    public TCPServerSSL(User owner, Context context) {
        this.owner = owner;
        this.context = context;
        initServer();
    }

    private void initServer() {
        try {
            SSLContext sslContext = SSLProvider
                    .getSSLContextForCertificateFile(this.context, "server_finished.pem", keystorepass);
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(owner.getUserPort());
            socketServerThread = new SocketServerThread();
            socketServerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (sslServerSocket != null) {
            try {
                stop = true;
                sslServerSocket.close();
                socketServerThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //Separate Thread to run Server----------------------------------------------------------------------
    public class SocketServerThread extends Thread {
        @Override
        public void run() {
            try {
                if (!sslServerSocket.isClosed()) {
                    while (stop == false) {
                        //callReceiveMessage
                        connection = (SSLSocket) sslServerSocket.accept();
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

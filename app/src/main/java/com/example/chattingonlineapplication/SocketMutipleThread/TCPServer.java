package com.example.chattingonlineapplication.SocketMutipleThread;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;


//ServerToReceive message
public class TCPServer {

    private ReceiveMessage receiveMessage;
    private Socket connection;
    private ServerSocket serverSocket;

    private boolean stop;
    private User contactUser;
    private User connectedUser;
    private List<MessageItem> listMessageItems;
    private RecyclerView recyclerView;
    private Thread socketServer;

    public TCPServer(User contactUser, User connectedUser, List<MessageItem> listMessageItems, RecyclerView recyclerView) {
        this.contactUser = contactUser;
        this.connectedUser = connectedUser;
        this.listMessageItems = listMessageItems;
        this.recyclerView = recyclerView;

        socketServer = new SocketServerThread();
        socketServer.start();
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                stop = true;
                socketServer.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Separate Thread to run Server
    class SocketServerThread extends Thread {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(contactUser.getUserPort());
                if (!serverSocket.isClosed()) {
                    while (stop == false) {
                        //callReceiveMessage
                        connection = serverSocket.accept();
                        connection.getReuseAddress();
                        Log.i("Connection", connection.toString());
                        //Async to handle each user's request.
                        if (connection != null)
                            receiveMessage = (ReceiveMessage) new ReceiveMessage().execute(connection);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class ReceiveMessage extends AsyncTask<Socket, Void, String> {
        String text;
        @Override
        protected String doInBackground(Socket... sockets) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String message;
                while ((message = br.readLine()) != null) {
                    stringBuilder.append("\n" + message);
                }
                text = stringBuilder.toString().trim();
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            //show Message
            //return message when server get
            listMessageItems.add(new MessageItem("1", connectedUser, contactUser, s, new Date().getTime(), 0, "1"));
            recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        }
    }
}


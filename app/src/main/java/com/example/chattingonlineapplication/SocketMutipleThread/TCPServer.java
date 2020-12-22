package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private class ClientRequestHandler extends Thread {

        private Socket socket;
        private IUpDateChatViewRecycler iUpDateChatViewRecycler;
        private StringBuilder stringBuilder;

        public ClientRequestHandler(Socket socket, IUpDateChatViewRecycler i) {
            this.socket = socket;
            this.iUpDateChatViewRecycler = i;
        }

        @Override
        public void run() {
            String text;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                stringBuilder = new StringBuilder();
                String message;
                while ((message = br.readLine()) != null) {
                    stringBuilder.append("\n" + message);
                }
                text = stringBuilder.toString().trim();
                //Callback event
                if (iUpDateChatViewRecycler != null && !text.trim().isEmpty()) {
                    Log.d("Client:", socket.getInetAddress().getHostAddress() + "");
                    FireStoreOpenConnection
                            .getInstance()
                            .getAccessToFireStore()
                            .collection(IInstanceDataBaseProvider.userCollection)
                            .whereEqualTo("userIpAddress", socket.getInetAddress().getHostAddress())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.toObjects(User.class).size() > 0) {
                                        iUpDateChatViewRecycler.updateItem(queryDocumentSnapshots.toObjects(User.class).get(0), text);
                                    }
                                }
                            });
                }
                Log.i("Text", text);
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




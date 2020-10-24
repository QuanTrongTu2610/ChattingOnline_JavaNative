package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientRequestHandler extends Thread {

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


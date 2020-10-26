package com.example.chattingonlineapplication.SocketMutipleThread;

import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

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
//                SocketAddress address = new InetSocketAddress(connectedUser.getUserIpAddress(), connectedUser.getUserPort());
//                socket = new Socket();
//                socket.connect(address, 4000);
                SocketAddress address = new InetSocketAddress("192.168.137.233", 8000);
                socket = new Socket();
                socket.connect(address, 3000);

                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(message);
                printWriter.flush();
                outputStream.close();
                socket.close();
                if (iUpDateChatViewRecycler != null) {
                    FireStoreOpenConnection
                            .getInstance()
                            .getAccessToFireStore()
                            .collection(IInstanceDataBaseProvider.userCollection)
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot != null)
                                        iUpDateChatViewRecycler.updateItem(documentSnapshot.toObject(User.class), message);
                                }
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

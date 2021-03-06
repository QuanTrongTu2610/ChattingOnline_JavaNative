package com.example.chattingonlineapplication.SSLSocketMutipleThread;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPClient;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.Utils.SSL.SSLProvider;
import com.example.chattingonlineapplication.Utils.SSL.TLSSocketFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TCPClientSSL {
    public SSLSocket sslsocket = null;
    private User connectedUser;
    private SendingMessage clientThread;
    private IUpDateChatViewRecycler iUpDateChatViewRecycler;
    private final char[] keystorepass = "123456789".toCharArray();
    private final static String Tag = TCPClientSSL.class.getSimpleName();
    private Context context;

    public TCPClientSSL(User connectedUser, Context context) {
        Log.i("Client Creating...", connectedUser.getUserIpAddress());
        this.connectedUser = connectedUser;
        this.context = context;
    }

    public void registerUpdateChatViewRecyclerEvent(IUpDateChatViewRecycler i) {
        this.iUpDateChatViewRecycler = i;
    }

    public void sendingMessage(String message) {
        clientThread = new SendingMessage(message, context);
        clientThread.start();
    }

    public void onDestroy() {
        if (sslsocket != null) {
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
        private Context context;

        public SendingMessage(String m, Context context) {
            this.message = m;
            this.context = context;
        }

        @Override
        public void run() {
            super.run();
            try {
                SSLContext sslContext = SSLProvider
                        .getSSLContextForCertificateFile(context, "mycert2.cer", "ConvertedSSLStore", "ConvertedTrustStore2", "123456789".toCharArray());
                SSLSocketFactory sslSocketFactory = new TLSSocketFactory(sslContext);
                SSLSocket sslsocket = (SSLSocket) sslSocketFactory
                        .createSocket(connectedUser.getUserIpAddress(), connectedUser.getUserPort());
                if (!message.trim().isEmpty()) {
                    OutputStream outputStream = sslsocket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream);
                    printWriter.println(message);
                    printWriter.flush();
                    outputStream.close();
                    sslsocket.close();
                }
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

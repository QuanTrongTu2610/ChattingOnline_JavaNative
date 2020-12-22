package com.example.chattingonlineapplication.SSLSocketMutipleThread;

import android.content.Context;
import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.Utils.SSL.SSLProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
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
        socketServerThread = new SocketServerThread();
        socketServerThread.start();
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
                SSLContext sslContext = SSLContext.getInstance("TLSv1");
                // Get a key manager factory using the default algorithm
                KeyManagerFactory kmf = KeyManagerFactory
                        .getInstance(KeyManagerFactory.getDefaultAlgorithm());

                // Load the PKCS12 key chain
                KeyStore keyStore = SSLProvider.getKeyStore(context, "mycert2.cer", "ConvertedSSLStore", "123456789".toCharArray());
                kmf.init(keyStore, "123456789".toCharArray());
                sslContext.init(kmf.getKeyManagers(), null, null);

                SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
                sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(owner.getUserPort());

                if (!sslServerSocket.isClosed()) {
                    while (stop == false) {
                        //callReceiveMessage
                        connection = (SSLSocket) sslServerSocket.accept();
                        Log.i("Connection", connection.toString());
                        //Async to handle each user's request.
                        Log.i("Connection Status", " " + connection.isConnected());
                        if (connection != null && connection.isConnected()) {
                            new ClientRequestHandler2(connection, iUpDateChatViewRecycler).start();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ClientRequestHandler2 extends Thread {

        private SSLSocket socket;
        private IUpDateChatViewRecycler iUpDateChatViewRecycler;
        private StringBuilder stringBuilder;

        public ClientRequestHandler2(SSLSocket socket, IUpDateChatViewRecycler i) {
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

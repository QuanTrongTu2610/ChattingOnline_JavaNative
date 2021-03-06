package com.example.chattingonlineapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Database.FireStore.ConversationDao;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.SSLSocketMutipleThread.TCPClientSSL;
import com.example.chattingonlineapplication.SSLSocketMutipleThread.TCPServerSSL;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.Utils.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Utils.SSL.SSLProvider;
import com.example.chattingonlineapplication.Utils.SSL.TLSSocketFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleChatScreenActivity extends AppCompatActivity {

    private static String TAG = SingleChatScreenActivity.class.getSimpleName();
    private TCPServerSSL tcpServer;
    private TCPClientSSL tcpClient;
    private EditText edittext_chatbox;
    private ImageButton button_chatbox_send;
    private Toolbar toolbarMessaging;
    private ActionBar actionBar;
    private RecyclerView reyclerviewListMessage;
    private CircleImageView imgReceiverAvatar;
    private TextView tvReceiverName;
    private TextView tvReceiverIsOnline;
    private ListMessageAdapter listMessageAdapter;
    private List<MessageItem> listMessageItems;
    private User owner;
    private User connectedUser;
    private String conversationId;
    private AlertDialog alertDialog;
    private ConversationDao conversationDao;
    private CheckingServerReachable checkingServerReachable;
    private LoadingMessageFromFireStore loadingMessageFromFireStore;
    private final static String Tag = SingleChatScreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        try {
            reflection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Get User Information
        try {
            owner = (User) getIntent().getSerializableExtra("USER_OWNER");
            connectedUser = (User) getIntent().getSerializableExtra("USER_CONNECTED");
            conversationId = getIntent().getStringExtra("CONVERSATION_ID");
            Log.i(TAG, "ConnectedUser: " + connectedUser.getUserFirstName());
            Log.i(TAG, "owner: " + owner.getUserFirstName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        createConversation();

//        CreateServerAndClient
        openServerAndClient();

        //Initialize ToolBar
        setupToolBar();

        //Create Adapter
        setupRecyclerView();

        //LoadingMessage
        loadingMessageFromFireStore = new LoadingMessageFromFireStore();
        loadingMessageFromFireStore.execute();

        //Sending Message
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edittext_chatbox.getText().toString().trim().isEmpty()) {
                    tcpClient.sendingMessage(edittext_chatbox.getText().toString().trim());
                    edittext_chatbox.setText("");
                }
            }
        });

        //EditText event scroll to new message
        edittext_chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reyclerviewListMessage.scrollToPosition(0);
            }
        });

        // CheckUserIsOnline
        checkingServerReachable = new CheckingServerReachable();
        checkingServerReachable.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupToolBar() {
        if (connectedUser != null) {
            if (!connectedUser.getUserAvatarUrl().isEmpty())
                Picasso.get().load(connectedUser.getUserAvatarUrl()).into(imgReceiverAvatar);
            tvReceiverName.setText(connectedUser.getUserFirstName() + " " + connectedUser.getUserLastName());
            tvReceiverIsOnline.setText("Loading..........");
        }
        //Toolbar Customize
        setSupportActionBar(toolbarMessaging);
        actionBar = getSupportActionBar();

        //back
        toolbarMessaging.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shutDownScreen();
    }

    private void shutDownScreen() {
        if (tcpClient != null) tcpClient.onDestroy();
        if (tcpServer != null) tcpServer.onDestroy();
        if (checkingServerReachable != null) {
            checkingServerReachable.cancel(false);
        }
        if (loadingMessageFromFireStore != null) {
            loadingMessageFromFireStore.cancel(true);
        }
    }

    private void setupRecyclerView() {
        listMessageAdapter = new ListMessageAdapter(this, listMessageItems, FirebaseAuth.getInstance().getCurrentUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        reyclerviewListMessage.setHasFixedSize(true);
        reyclerviewListMessage.setLayoutManager(linearLayoutManager);
        reyclerviewListMessage.setAdapter(listMessageAdapter);
    }

    private void reflection() throws IOException {
        toolbarMessaging = findViewById(R.id.toolbarMessaging);
        reyclerviewListMessage = findViewById(R.id.reyclerviewListMessage);
        imgReceiverAvatar = findViewById(R.id.imgReceiverAvatar);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        tvReceiverIsOnline = findViewById(R.id.tvReceiverIsOnline);
        connectedUser = null;
        owner = null;
        listMessageItems = new ArrayList<>();
        conversationDao = new ConversationDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }

    //Create Server and client
    private void openServerAndClient() {
        try {
//            //Host Server
            tcpServer = new TCPServerSSL(owner, this);
            tcpServer.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
                @Override
                public void updateItem(User user, final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listMessageItems.add(
                                    new MessageItem("1",
                                            connectedUser,
                                            owner,
                                            message,
                                            (new Timestamp(System.currentTimeMillis())).getTime(),
                                            0,
                                            "1")
                            );
                            reyclerviewListMessage.getAdapter().notifyDataSetChanged();
                            reyclerviewListMessage.scrollToPosition(reyclerviewListMessage.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            });

//            Client
            tcpClient = new TCPClientSSL(connectedUser, this);
            tcpClient.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
                @Override
                public void updateItem(User user, String message) {
                    try {
                        addMessageFirestore(owner, connectedUser, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listMessageItems.add(
                                    new MessageItem("1",
                                            owner,
                                            connectedUser,
                                            message,
                                            (new Timestamp(System.currentTimeMillis())).getTime(),
                                            0,
                                            "1")
                            );
                            reyclerviewListMessage.getAdapter().notifyDataSetChanged();
                            reyclerviewListMessage.scrollToPosition(reyclerviewListMessage.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessageFirestore(User userSender, User userReceiver, String message) throws IOException {
        Message m = new Message(autoGenId(IInstanceDataBaseProvider.messageCollection),
                userSender.getUserId(),
                userReceiver.getUserId(),
                message,
                new Timestamp(System.currentTimeMillis()).getTime(),
                0
        );

        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.conversationCollection)
                    .document(conversationId)
                    .update("messages", FieldValue.arrayUnion(m));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sortPaticipants
    private ArrayList<String> sortString(ArrayList<String> a) {
        ArrayList<String> arrayList = new ArrayList<>(a);
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).compareTo(arrayList.get(j)) > 0) {
                    String temp = arrayList.get(j - 1);
                    arrayList.set(j - 1, arrayList.get(j));
                    arrayList.set(j, temp);
                }
            }
        }
        return arrayList;
    }

    private String autoGenId(String name) throws IOException {
        return FireStoreOpenConnection.getInstance().getAccessToFireStore()
                .collection(name)
                .document()
                .getId();
    }

    private void createConversation() {
        alertDialog = LoadingDialog.getInstance().getDialog(this);
        alertDialog.show();
        try {
            Conversation conversation = new Conversation(
                    conversationId,
                    "Conversation",
                    sortString(new ArrayList<String>(Arrays.asList(owner.getUserId(), connectedUser.getUserId()))),
                    new ArrayList<Message>()
            );

            conversationDao
                    .get(conversationId)
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Conversation c = (Conversation) documentSnapshot.toObject(Conversation.class);
                            if (c == null) {
                                try {
                                    conversationDao
                                            .create(conversation)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    alertDialog.dismiss();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    alertDialog.dismiss();
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                alertDialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismiss();
                        }
                    })
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Ping to
    // Connected Client
    class CheckingServerReachable extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (!isCancelled()) {
                try {
                    while (!isCancelled()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSocketAlive(connectedUser.getUserIpAddress(), connectedUser.getUserPort())) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvReceiverIsOnline.setText("Client Ip: " + connectedUser.getUserIpAddress() + " port: " + connectedUser.getUserPort());
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvReceiverIsOnline.setText("This user is out of server");
                                        }
                                    });
                                }
                            }
                        }).start();
                        Thread.sleep(4000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private boolean isSocketAlive(String hostname, int port) {
        try {
            SSLContext sslContext = SSLProvider
                    .getSSLContextForCertificateFile(this, "mycert2.cer", "ConvertedSSLStore", "ConvertedTrustStore2", "123456789".toCharArray());
            SSLSocketFactory sslSocketFactory = new TLSSocketFactory(sslContext);
            SSLSocket sslsocket = (SSLSocket) sslSocketFactory
                    .createSocket(hostname, port);
            sslsocket.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    class LoadingMessageFromFireStore extends AsyncTask<Void, Void, Void> {

        private MessageItem messageItem;
        private User userSender;
        private User userReceiver;
        private ArrayList<MessageItem> lstLoadDataBase;

        public LoadingMessageFromFireStore() {
            lstLoadDataBase = new ArrayList<>();
        }

        @Override

        protected Void doInBackground(Void... voids) {
            if (!isCancelled()) {
                try {
                    conversationDao
                            .get(conversationId)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Conversation conversation = documentSnapshot.toObject(Conversation.class);
                                    if (conversation != null) {
                                        ArrayList<Message> lst = conversation.getMessages();
                                        for (Message m : lst) {
                                            if (m.getUserSenderId().equals(owner.getUserId())) {
                                                userSender = owner;
                                                userReceiver = connectedUser;
                                            } else {
                                                userSender = connectedUser;
                                                userReceiver = owner;
                                            }

                                            messageItem = new MessageItem(
                                                    m.getMessageId(),
                                                    userSender,
                                                    userReceiver,
                                                    m.getContent(),
                                                    m.getMessageDateCreated(),
                                                    m.getMessageSeenAt(),
                                                    conversation.getConversationId()
                                            );
                                            lstLoadDataBase.add(messageItem);
                                        }

                                        //Cache Current Array
                                        ArrayList<MessageItem> cache = new ArrayList<>(listMessageItems);
                                        listMessageItems.removeAll(listMessageItems);
                                        listMessageItems.addAll(lstLoadDataBase);
                                        listMessageItems.addAll(cache);
                                        reyclerviewListMessage.getAdapter().notifyDataSetChanged();
                                        reyclerviewListMessage.scrollToPosition(reyclerviewListMessage.getAdapter().getItemCount() - 1);
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
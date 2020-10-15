package com.example.chattingonlineapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Socket.Client;
import com.example.chattingonlineapplication.Socket.Server;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPClient;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingScreenActivity extends AppCompatActivity {

    private String TAG = "CLIENT ACTIVITY";
    private boolean isCreateConversation;

    private Server server;
    private Client client;
    private EditText edittext_chatbox;
    private ImageButton button_chatbox_send;
    private Toolbar toolbarMessaging;
    private ActionBar actionBar;
    private RecyclerView reyclerviewListMessage;
    private CircleImageView imgReceiverAvatar;
    private TextView tvReceiverName;
    private TextView tvReceiverIsOnline;

    private ListMessageAdapter listMessageAdapter;
    private List<MessageItem> messageItems;
    private User contactUser;
    private User connectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();
        //Check Does the conversation is exist
//        try {
//            FireStoreOpenConnection.getInstance()
//                    .getAccessToFireStore()
//                    .collection("conversation")
//                    .whereIn("participants", new ArrayList<String>(Arrays.asList(connectedUser.getUserId(), contactUser.getUserId())));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        //CreateServer
        connectToUser();

        //Initialize ToolBar
        setupToolBar();

        //Create Adapter
        setupRecyclerView();


        //Socket client = new Socket(connectedUser.getUserIpAddress(), connectedUser.getUserPort());
        try {
            final Socket client = new Socket("192.168.43.198", 8000);
            button_chatbox_send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mess = edittext_chatbox.getText().toString().trim();
                    if (!mess.isEmpty()) {
//                        try {
//                            client = new Client(
//                                    contactUser,
//                                    connectedUser,
//                                    messageItems,
//                                    listMessageAdapter,
//                                    reyclerviewListMessage,
//                                    edittext_chatbox
//                            );
//                            client.execute();
//                        } catch (Exception e) {
//                            e.printStackTrace();
                        TCPClient tcpClient = new TCPClient(client);
                        tcpClient.execute(mess);
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

        //EditText event scroll to new message
        edittext_chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reyclerviewListMessage.scrollToPosition(0);
            }
        });

        //EditText event all to open send button
        edittext_chatbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                button_chatbox_send.setFocusable(!editable.toString().trim().isEmpty());
            }
        });
    }

    private void setupToolBar() {
        if (connectedUser != null) {
            Picasso.get().load(connectedUser.getUserAvatarUrl()).into(imgReceiverAvatar);
            tvReceiverName.setText(connectedUser.getUserFirstName() + " " + connectedUser.getUserLastName());
            tvReceiverIsOnline.setText("User is online");
        }
        //Toolbar Customize
        setSupportActionBar(toolbarMessaging);
        actionBar = getSupportActionBar();
        toolbarMessaging.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server.shutdownServer();
                server.interrupt();
                if (client != null) {
                    client.cancel(true);
                }
                finish();
            }
        });
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
    }

    private void setupRecyclerView() {
        listMessageAdapter = new ListMessageAdapter(this, messageItems, FirebaseAuth.getInstance().getCurrentUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        reyclerviewListMessage.setHasFixedSize(true);
        reyclerviewListMessage.setLayoutManager(linearLayoutManager);
        reyclerviewListMessage.setAdapter(listMessageAdapter);
    }

    private void reflection() {
        toolbarMessaging = findViewById(R.id.toolbarMessaging);
        reyclerviewListMessage = findViewById(R.id.reyclerviewListMessage);
        imgReceiverAvatar = findViewById(R.id.imgReceiverAvatar);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        tvReceiverIsOnline = findViewById(R.id.tvReceiverIsOnline);
        connectedUser = null;
        contactUser = null;
        messageItems = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }

    //Create Server
    public void connectToUser() {
//        server = new Server(contactUser, connectedUser, messageItems, listMessageAdapter, reyclerviewListMessage);
//        server.start();
        try {
            ServerSocket serverSocket = new ServerSocket(contactUser.getUserPort());
            TCPServer tcpServer = new TCPServer(serverSocket);
            tcpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
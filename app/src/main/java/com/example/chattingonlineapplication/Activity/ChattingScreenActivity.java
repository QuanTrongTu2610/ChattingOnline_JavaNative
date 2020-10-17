package com.example.chattingonlineapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Socket.Client;
import com.example.chattingonlineapplication.Socket.Server;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPClient;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingScreenActivity extends AppCompatActivity {

    private String TAG = "CLIENT ACTIVITY";
    private boolean isCreateConversation;

    private TCPServer tcpServer;
    private TCPClient tcpClient;

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
    private User contactUser;
    private User connectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();
        //Get User Information
        try {
            contactUser = (User) getIntent().getSerializableExtra("USER_CONTACT");
            connectedUser = (User) getIntent().getSerializableExtra("USER_CONNECTED");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //CreateServer
        openServer();

        //Initialize ToolBar
        setupToolBar();

        //Create Adapter
        setupRecyclerView();

        //Sending Message
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tcpClient = new TCPClient(contactUser,
                            connectedUser,
                            edittext_chatbox.getText().toString().trim(),
                            listMessageItems,
                            reyclerviewListMessage,
                            edittext_chatbox
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tcpClient.execute();
            }
        });


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
                button_chatbox_send.setClickable(!editable.toString().trim().isEmpty());
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

        //back
        toolbarMessaging.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tcpClient != null) tcpClient.cancel(true);
                if (tcpClient != null) tcpServer.onDestroy();
                finish();
            }
        });
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);
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
        listMessageItems = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }

    //Create Server
    public void openServer() {
        try {
            Log.i("Create Server", "Created");
            tcpServer = new TCPServer(contactUser, connectedUser, listMessageItems, reyclerviewListMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
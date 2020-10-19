package com.example.chattingonlineapplication.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.chattingonlineapplication.Database.FireStore.ConversationDao;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPClient;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingScreenActivity extends AppCompatActivity {

    private String TAG = "CLIENT ACTIVITY";
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
    private String contactId;
    private String conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();
        //Get User Information
        try {
            contactUser = (User) getIntent().getSerializableExtra("USER_CONTACT");
            connectedUser = (User) getIntent().getSerializableExtra("USER_CONNECTED");
            contactId = (String) getIntent().getStringExtra("CONTACT_ID");
            conversationId = (String) getIntent().getStringExtra("CONVERSATION_ID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //CreateServer
        openServerAndClient();

        //Initialize ToolBar
        setupToolBar();

        //Create Adapter
        setupRecyclerView();

        //Sending Message
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tcpClient.sendingMessage(edittext_chatbox.getText().toString().trim());
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
                if (tcpClient != null) tcpClient.onDestroy();
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
    private void openServerAndClient() {
        try {
            tcpServer = new TCPServer(contactUser);
            tcpServer.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
                @Override
                public void updateItem(final String message) {
                    //Update Message to Database: TODO
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listMessageItems.add(
                                    new MessageItem("1",
                                            connectedUser,
                                            contactUser,
                                            message,
                                            (new Timestamp(System.currentTimeMillis())).getTime(),
                                            0,
                                            "1")
                            );
                            reyclerviewListMessage.getAdapter().notifyDataSetChanged();
                            edittext_chatbox.setText("");
                            reyclerviewListMessage.scrollToPosition(reyclerviewListMessage.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            });

            tcpClient = new TCPClient(connectedUser);
            tcpClient.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
                //Update Message to Database: TODO
                @Override
                public void updateItem(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listMessageItems.add(
                                    new MessageItem("1",
                                            contactUser,
                                            connectedUser,
                                            message,
                                            (new Timestamp(System.currentTimeMillis())).getTime(),
                                            0,
                                            "1")
                            );
                            reyclerviewListMessage.getAdapter().notifyDataSetChanged();
                            edittext_chatbox.setText("");
                            reyclerviewListMessage.scrollToPosition(reyclerviewListMessage.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String autoGenString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }



    //ToDo
    private void addMessage(User userSender, User userReceiver, String message) {
        Message m = new Message(autoGenString(),
                userSender.getUserId(),
                userReceiver.getUserId(),
                message,
                new Timestamp(System.currentTimeMillis()).getTime(),
                0
        );

        try {
            ConversationDao conversationDao = new ConversationDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
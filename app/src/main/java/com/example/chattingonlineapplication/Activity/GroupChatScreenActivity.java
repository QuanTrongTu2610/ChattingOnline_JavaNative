package com.example.chattingonlineapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.BroadCast.UserParticipantReceiver;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.GroupChat;
import com.example.chattingonlineapplication.Models.Item.GroupChatItem;
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Utils.Interface.IUpDateChatViewRecycler;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Services.GroupParticipantService;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPClient;
import com.example.chattingonlineapplication.SocketMutipleThread.TCPServer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatScreenActivity extends AppCompatActivity {

    private static final String TAG = GroupChatScreenActivity.class.getSimpleName();
    private ArrayList<MessageItem> listMessageItems;
    private Toolbar toolbarGroupMessaging;
    private CircleImageView imgGroupAvatar;
    private TextView tvGroupTitle;
    private TextView tvGroupId;
    private RecyclerView recyclerViewListMessage;
    private EditText edittext_chatbox;
    private ImageButton button_chatbox_send;
    private TCPServer tcpServer;
    private TCPClient tcpClient;
    private LinearLayoutManager linearLayoutManager;
    private ListMessageAdapter listMessageAdapter;
    private GroupChatItem groupChatItem;
    private String currentMessage = new String();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private GroupParticipantService groupParticipantService;
    private ServiceConnection serviceConnection;
    private UserParticipantReceiver userParticipantReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_screen);
        //binding View
        reflection();

        if (getIntent() != null) {
            try {
                groupChatItem = (GroupChatItem) getIntent().getSerializableExtra("GROUPCHAT_ITEM");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // setUpListView
        setUpListMessageView();

        // setUpToolBar
        setupToolBar();

        // check and add user to participants
        checkUserIsExist();

        // openSocketConnection
        getOwnerInformationBeforeHostServer();

        // start notification-foreground service
        startGroupChatService();

        // send message
        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edittext_chatbox.getText().toString().trim().isEmpty()) {
                    String message = edittext_chatbox.getText().toString().trim();
                    try {
                        FireStoreOpenConnection
                                .getInstance()
                                .getAccessToFireStore()
                                .collection(IInstanceDataBaseProvider.groupCollection)
                                .document(groupChatItem.getGroupId())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        GroupChat g = documentSnapshot.toObject(GroupChat.class);
                                        g.getParticipants().remove(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        for (int i = 0; i < g.getParticipants().size(); i++) {
                                            sendMessage(g.getParticipants().get(i), message);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // register broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UserAddNotification");
        userParticipantReceiver = new UserParticipantReceiver();
        LocalBroadcastManager.getInstance(GroupChatScreenActivity.this).registerReceiver(userParticipantReceiver, intentFilter);
    }

    private void startGroupChatService() {
        // bind service
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // call when service is connected
                Log.i(TAG, "Group Service Connect");
                groupParticipantService = ((GroupParticipantService.GroupParticipantBinder) service).getService();
                groupParticipantService.realtimeDatabaseListener(GroupChatScreenActivity.this, groupChatItem);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // call when service is stopped
                Log.i(TAG, "Group Service Disconnected");
            }
        };

        // call service
        Intent intent = new Intent(GroupChatScreenActivity.this, GroupParticipantService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private void sendMessage(String connectedUserId, String message) {
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.userCollection)
                    .document(connectedUserId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User connectedUser = documentSnapshot.toObject(User.class);
                            openClient(connectedUser, message);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reflection() {
        toolbarGroupMessaging = findViewById(R.id.toolbarGroupMessaging);
        imgGroupAvatar = findViewById(R.id.imgGroupAvatar);
        tvGroupTitle = findViewById(R.id.tvGroupTitle);
        tvGroupId = findViewById(R.id.tvGroupId);
        recyclerViewListMessage = findViewById(R.id.recyclerViewListMessage);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        listMessageItems = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void setUpListMessageView() {
        linearLayoutManager = new LinearLayoutManager(this);
        listMessageAdapter = new ListMessageAdapter(this, listMessageItems, FirebaseAuth.getInstance().getCurrentUser());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerViewListMessage.setHasFixedSize(true);
        recyclerViewListMessage.setLayoutManager(linearLayoutManager);
        recyclerViewListMessage.setAdapter(listMessageAdapter);
    }

    //Host Server to receive message
    private void getOwnerInformationBeforeHostServer() {
        ExecutorService e = Executors.newSingleThreadExecutor();
        e.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FireStoreOpenConnection
                            .getInstance()
                            .getAccessToFireStore()
                            .collection(IInstanceDataBaseProvider.userCollection)
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User owner = documentSnapshot.toObject(User.class);
                                    // open server to listen message.
                                    openServer(owner);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // host server
    private void openServer(User owner) {
        try {
            //Host Server
            tcpServer = new TCPServer(owner);
            tcpServer.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
                @Override
                public void updateItem(User user, final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listMessageItems.add(
                                    new MessageItem("1",
                                            user,
                                            owner,
                                            message,
                                            (new Timestamp(System.currentTimeMillis())).getTime(),
                                            0,
                                            "1")
                            );
                            recyclerViewListMessage.getAdapter().notifyItemInserted(listMessageItems.size());
                            edittext_chatbox.setText("");
                            recyclerViewListMessage.scrollToPosition(recyclerViewListMessage.getAdapter().getItemCount() - 1);
                        }
                    });
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // open Client to send message
    private void openClient(User connectedUser, String message) {
        tcpClient = new TCPClient(connectedUser);
        tcpClient.registerUpdateChatViewRecyclerEvent(new IUpDateChatViewRecycler() {
            @Override
            public void updateItem(User user, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!currentMessage.equals(message)) {
                            MessageItem m = new MessageItem("1",
                                    user,
                                    new User(),
                                    message,
                                    (new Timestamp(System.currentTimeMillis())).getTime(),
                                    0,
                                    "1"
                            );
                            currentMessage = message;
                            listMessageItems.add(m);
                            recyclerViewListMessage.getAdapter().notifyDataSetChanged();
                            edittext_chatbox.setText("");
                            recyclerViewListMessage.scrollToPosition(recyclerViewListMessage.getAdapter().getItemCount() - 1);
                        }
                    }
                });
            }
        });
        tcpClient.sendingMessage(message);
    }

    // setupToolbar
    private void setupToolBar() {
        setSupportActionBar(toolbarGroupMessaging);
        toolbarGroupMessaging.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar a = getSupportActionBar();
        a.setDisplayHomeAsUpEnabled(true);
        a.setTitle(null);

        if (!groupChatItem.getAvatarUrl().isEmpty()) {
            Picasso.get().load(groupChatItem.getAvatarUrl()).into(imgGroupAvatar);
        }
        tvGroupTitle.setText(groupChatItem.getTitle());
        tvGroupId.setText(groupChatItem.getGroupId());
    }


    private void shutDownScreen() {
        if (tcpClient != null) tcpClient.onDestroy();
        if (tcpServer != null) tcpServer.onDestroy();
        //Delete from realtime database
        deleteUserFromRealtimeDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    // delete User from Realtime database
    private void deleteUserFromRealtimeDatabase(String userId) {
        if (databaseReference != null) {
            databaseReference
                    .child("GroupChat")
                    .child(groupChatItem.getGroupId())
                    .child("participants")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "DeleteRealTimeUser: " + "Success....");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    //add User to Realtime database
    private void addToRealtimeDatabase(String userId) {
        //Add to Realtime
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.child("GroupChat").child(groupChatItem.getGroupId()).child("participants").setValue(hashMap);
    }

    private void checkUserIsExist() {
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.groupCollection)
                    .document(groupChatItem.getGroupId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            GroupChat g = documentSnapshot.toObject(GroupChat.class);
                            if (g != null) {
                                if (!g.getParticipants().contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    //Add User To Participants
                                    addToParticipants(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    addToRealtimeDatabase(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToParticipants(String userId) {
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.groupCollection)
                    .document(groupChatItem.getGroupId())
                    .update("participants", FieldValue.arrayUnion(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "GroupParticipants: " + "Add User Success");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (tcpClient != null) tcpClient.onDestroy();
            if (tcpServer != null) tcpServer.onDestroy();
            //DeleteUserFromParticipants
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.groupCollection)
                    .document(groupChatItem.getGroupId())
                    .update("participants", FieldValue.arrayRemove(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "GroupParticipants: " + "Delete Success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // shut down
        shutDownScreen();

        // unbind service
        unbindService(serviceConnection);

        //unregister
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userParticipantReceiver);
    }
}
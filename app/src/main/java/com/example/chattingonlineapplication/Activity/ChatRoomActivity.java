package com.example.chattingonlineapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.chattingonlineapplication.Adapter.ListGroupChatAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.GroupChat;
import com.example.chattingonlineapplication.Models.Item.GroupChatItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatRoomActivity extends AppCompatActivity {

    private ArrayList<GroupChatItem> lstGroupChatItem;
    private Toolbar toolbarChattingGroup;
    private RecyclerView recyclerGroup;
    private FloatingActionButton btnCreateGroup;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        reflection();
        //setup ToolBar
        setupToolBar();
        //setup RecyclerView
        setupRecyclerView();
        //btnAddGroupEvent
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomActivity.this, RegisterGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void reflection() {
        lstGroupChatItem = new ArrayList<>();
        toolbarChattingGroup = findViewById(R.id.toolbarChattingGroup);
        recyclerGroup = findViewById(R.id.recyclerGroup);
        btnCreateGroup = findViewById(R.id.btnCreateGroup);
    }

    private void setupRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        ListGroupChatAdapter adapter = new ListGroupChatAdapter(this, lstGroupChatItem);
        recyclerGroup.setLayoutManager(linearLayoutManager);
        recyclerGroup.setAdapter(adapter);
    }

    //setupToolbar
    private void setupToolBar() {
        setSupportActionBar(toolbarChattingGroup);
        toolbarChattingGroup.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar a = getSupportActionBar();
        a.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(lstGroupChatItem.size() > 0) {
            lstGroupChatItem.removeAll(lstGroupChatItem);
            recyclerGroup.getAdapter().notifyDataSetChanged();
            Log.i("Size", lstGroupChatItem.size() + "");
        }
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.groupCollection)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<GroupChat> list = queryDocumentSnapshots.toObjects(GroupChat.class);
                            Log.i("GroupSize:", list.size() + "");

                            if (list.size() > 0) {
                                GroupChat groupChat;
                                for (int i = 0; i < list.size(); i++) {
                                    groupChat = list.get(i);
                                    updateToListGroupItem(groupChat);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateToListGroupItem(GroupChat g) {
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.userCollection)
                    .document(g.getGroupAuthorId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                GroupChatItem item = new GroupChatItem(
                                        g.getGroupId(),
                                        g.getGroupTitle(),
                                        g.getGroupAvatarUrl(),
                                        user,
                                        updateParticipant(g.getParticipants())
                                );
                                lstGroupChatItem.add(item);
                                recyclerGroup.getAdapter().notifyDataSetChanged();
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

    private ArrayList<User> updateParticipant(ArrayList<String> l) {
        ArrayList<User> u = new ArrayList<>();
        ExecutorService e = Executors.newSingleThreadExecutor();
        for (int i = 0; i < l.size(); i++) {
            String userId = l.get(i);
            e.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        FireStoreOpenConnection
                                .getInstance()
                                .getAccessToFireStore()
                                .collection(IInstanceDataBaseProvider.userCollection)
                                .document(userId)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        u.add(documentSnapshot.toObject(User.class));
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return u;
    }

}
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
import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingScreenActivity extends AppCompatActivity {

    private List<MessageItem> lstMessage;

    private EditText edittext_chatbox;
    private ImageButton button_chatbox_send;
    private Toolbar toolbarMessaging;
    private ActionBar actionBar;
    private RecyclerView reyclerviewListMessage;
    private CircleImageView imgReceiverAvatar;
    private TextView tvReceiverName;
    private TextView tvReceiverIsOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();
        binding();

        setSupportActionBar(toolbarMessaging);
        actionBar = getSupportActionBar();

        toolbarMessaging.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

        ListMessageAdapter listMessageAdapter = new ListMessageAdapter(this, lstMessage, FirebaseAuth.getInstance().getCurrentUser());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        reyclerviewListMessage.setHasFixedSize(true);
        reyclerviewListMessage.setLayoutManager(linearLayoutManager);
        reyclerviewListMessage.setAdapter(listMessageAdapter);


        edittext_chatbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    button_chatbox_send.setVisibility(View.VISIBLE);
                } else {
                    button_chatbox_send.setVisibility(View.INVISIBLE);
                }
            }
        });

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void reflection() {
        toolbarMessaging = findViewById(R.id.toolbarMessaging);
        reyclerviewListMessage = findViewById(R.id.reyclerviewListMessage);
        imgReceiverAvatar = findViewById(R.id.imgReceiverAvatar);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        lstMessage = new ArrayList<>();
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }

    protected void binding() {
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "OaCGEcCW32a906jbXqUoab15WsA2",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
        lstMessage.add(new MessageItem("Pham Thi Ha",
                "none",
                "id",
                "s",
                "Hello, ban oi",
                1600392475539L,
                1600392475539L, "id"));
    }
}
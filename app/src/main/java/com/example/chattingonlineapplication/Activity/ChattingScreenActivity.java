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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListMessageAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.ICallBackUserProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Socket.Client;
import com.example.chattingonlineapplication.Socket.Server;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChattingScreenActivity extends AppCompatActivity {

    private String TAG = "CLIENT ACTIVITY";

    private EditText edittext_chatbox;
    private ImageButton button_chatbox_send;
    private Toolbar toolbarMessaging;
    private ActionBar actionBar;
    private RecyclerView reyclerviewListMessage;
    private CircleImageView imgReceiverAvatar;
    private TextView tvReceiverName;
    private TextView tvReceiverIsOnline;

    private User user;
    private User userPartner;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();
        //Setting
        userPartner = (User) getIntent().getSerializableExtra("USER_FRIEND");
        if (userPartner != null) {
            Picasso.get().load(userPartner.getUserAvatarUrl()).into(imgReceiverAvatar);
            tvReceiverName.setText(userPartner.getUserFirstName() + " " + userPartner.getUserLastName());
            tvReceiverIsOnline.setText("User is online");
        }

        connectToUser();

        button_chatbox_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = edittext_chatbox.getText().toString().trim();
                if (!mess.isEmpty()) {
                    try {
                        Client client = new Client(user.getUserIpAddress(),
                                userPartner.getUserPort(),
                                new Message("1",
                                        user,
                                        userPartner,
                                        mess,
                                        (new Timestamp(System.currentTimeMillis())).getTime(),
                                        0,
                                        "1"));
                        client.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //sendMessage
            }
        });

        //Toolbar Customize
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


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        reyclerviewListMessage.setHasFixedSize(true);
        reyclerviewListMessage.setLayoutManager(linearLayoutManager);


        //Sending
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
                //Send message
            }
        });
    }

    private void reflection() {
        toolbarMessaging = findViewById(R.id.toolbarMessaging);
        reyclerviewListMessage = findViewById(R.id.reyclerviewListMessage);
        imgReceiverAvatar = findViewById(R.id.imgReceiverAvatar);
        tvReceiverName = findViewById(R.id.tvReceiverName);
        button_chatbox_send = findViewById(R.id.button_chatbox_send);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        tvReceiverIsOnline = findViewById(R.id.tvReceiverIsOnline);
        userPartner = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }

    public void connectToUser() {
        final AlertDialog alertDialog = LoadingDialog.getInstance().getDialog(this);
        alertDialog.setMessage("Connect to User Ip: "
                + userPartner.getUserIpAddress()
                + "\n"
                + "Port: "
                + userPartner.getUserPort());
        alertDialog.show();

        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                            userDao.get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .continueWith(new Continuation<DocumentSnapshot, Object>() {
                                        @Override
                                        public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                            user = task.getResult().toObject(User.class);
                                            Log.i("user", user.getUserFirstName());
                                            Server server = new Server(user.getUserIpAddress(),
                                                    user.getUserPort(),
                                                    ChattingScreenActivity.this,
                                                    ChattingScreenActivity.this,
                                                    reyclerviewListMessage,
                                                    new ArrayList<Message>(),
                                                    new ListMessageAdapter(ChattingScreenActivity.this, new ArrayList<com.example.chattingonlineapplication.Models.Message>(),
                                                            FirebaseAuth.getInstance().getCurrentUser()));
                                            server.start();
                                            alertDialog.dismiss();
                                            service.shutdown();
                                            return null;
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

}
package com.example.chattingonlineapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.R;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolBarUserProfile;
    private RecyclerView recyclerProfileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reflection();

        setSupportActionBar(toolBarUserProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    }

    private void reflection() {
        toolBarUserProfile = findViewById(R.id.toolBarUserProfile);
        recyclerProfileUser = findViewById(R.id.recyclerProfileUser);
    }
}
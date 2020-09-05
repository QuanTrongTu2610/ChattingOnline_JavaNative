package com.example.chattingonlineapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListSettingOptionsUserProfileAdapter;
import com.example.chattingonlineapplication.Models.SettingUserProfileItemModel;
import com.example.chattingonlineapplication.R;
import com.google.android.material.appbar.AppBarLayout;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private List<SettingUserProfileItemModel> lst;

    private AppBarLayout appBarLayout;
    private Toolbar toolBarUserProfile;
    private RecyclerView recyclerProfileUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reflection();
        bindingData();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });

        setSupportActionBar(toolBarUserProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListSettingOptionsUserProfileAdapter adapter = new ListSettingOptionsUserProfileAdapter(this, lst);
        recyclerProfileUser.setLayoutManager(linearLayoutManager);
        recyclerProfileUser.setAdapter(adapter);

    }

    private void bindingData() {
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_notifications_24, "Notifications"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_lock_24, "Privacy and Security"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_bar_chart_24, "Data and Storage"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_message_24, "Chat Settings"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_folder_24, "Folders"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_devices_24, "Devices"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_language_24, "Language"));
    }

    private void reflection() {
        toolBarUserProfile = findViewById(R.id.toolBarUserProfile);
        recyclerProfileUser = findViewById(R.id.recyclerProfileUser);
        appBarLayout = findViewById(R.id.app_bar);
        lst = new ArrayList<>();
    }
}
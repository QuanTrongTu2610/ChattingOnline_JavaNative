package com.example.chattingonlineapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chattingonlineapplication.R;

import android.os.Bundle;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private Toolbar toolBarUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reflection();

        setSupportActionBar(toolBarUserProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void reflection() {
        toolBarUserProfile = findViewById(R.id.toolBarUserProfile);
    }
}
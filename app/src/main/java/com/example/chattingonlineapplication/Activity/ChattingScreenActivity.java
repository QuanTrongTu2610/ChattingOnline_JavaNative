package com.example.chattingonlineapplication.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;

import com.example.chattingonlineapplication.R;

public class ChattingScreenActivity extends AppCompatActivity {

    private Toolbar toolbarMessaging;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_screen);
        reflection();

        setSupportActionBar(toolbarMessaging);
        actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(null);

    }

    private void reflection() {
        toolbarMessaging = findViewById(R.id.toolbarMessaging);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatting_screen_menu, menu);
        return true;
    }
}
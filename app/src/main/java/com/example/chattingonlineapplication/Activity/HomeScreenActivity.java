package com.example.chattingonlineapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.chattingonlineapplication.Adapter.ListConversationsAdapter;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {


    private View viewHeader;
    private ImageView imgUserAvatar;
    private LinearLayoutManager linearLayoutManager;
    private ListConversationsAdapter listConversationsAdapter;
    private MaterialSearchView searchViewLayoutUserMessage;
    private ArrayList<Conversation> lstUserMessage;

    private RecyclerView recyclerUser;
    private Toolbar toolbarHomeScreen;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        reflection();
        bindingData();

        setSupportActionBar(toolbarHomeScreen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_black_24);

        if (imgUserAvatar != null) {
            imgUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        //
        linearLayoutManager = new LinearLayoutManager(this);
        listConversationsAdapter = new ListConversationsAdapter(this, lstUserMessage);
        recyclerUser.setLayoutManager(linearLayoutManager);
        recyclerUser.setAdapter(listConversationsAdapter);

    }

    private void bindingData() {
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Nguyen Anh Dat","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Le Duc Lam","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Vu Viet Dung","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Truong Tuan Truong","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Dam Sy Hoang","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Trinh Ngoc Son","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
        lstUserMessage.add(new Conversation(1, 1, "Hello, How are you ?", "July 7", "Quan Trong Tu","null"));
    }

    private void reflection() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbarHomeScreen = findViewById(R.id.toolbarHomeScreen);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarHomeScreen, R.string.open, R.string.close);
        recyclerUser = findViewById(R.id.recyclerUser);
        lstUserMessage = new ArrayList<>();
        searchViewLayoutUserMessage = findViewById(R.id.searchViewLayoutUserMessage);

        viewHeader = navigationView.getHeaderView(0);
        imgUserAvatar = viewHeader.findViewById(R.id.imgUserAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usermessage_mainscreen_menu, menu);
        executeSearch(menu);
        return true;
    }

    private void executeSearch(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.itemUserMessageSearch);
        searchViewLayoutUserMessage.setMenuItem(menuItem);
        searchViewLayoutUserMessage.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listConversationsAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
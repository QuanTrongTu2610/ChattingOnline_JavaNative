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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.chattingonlineapplication.Adapter.ListUserMessagesAdapter;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Models.UserMessage;
import com.example.chattingonlineapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private ListUserMessagesAdapter listUserMessagesAdapter;
    private MaterialSearchView searchViewLayoutUserMessage;
    private ArrayList<UserMessage> lstUserMessage;

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


        //
        linearLayoutManager = new LinearLayoutManager(this);
        listUserMessagesAdapter = new ListUserMessagesAdapter(this, lstUserMessage);
        recyclerUser.setLayoutManager(linearLayoutManager);
        recyclerUser.setAdapter(listUserMessagesAdapter);

    }

    private void bindingData() {
        lstUserMessage.add(new UserMessage(new User(1, "Quan Trong Tu", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Pham Thi Ha", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Nguyen Anh Dat", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Le Duc Lam", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Nguyen Huu Hoang", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Truong Tuan Truong", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Nguyen Anh Dung", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Trieu Thi Van", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Dam Sy Hoang", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
        lstUserMessage.add(new UserMessage(new User(1, "Trinh Ngoc Son", "123", "0832677917", "", "Hello", null), new Message("Hello1"), new Message("Nothing"), "7 July", false));
    }

    private void reflection() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbarHomeScreen = findViewById(R.id.toolbarHomeScreen);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarHomeScreen, R.string.open, R.string.close);
        recyclerUser = findViewById(R.id.recyclerUser);
        lstUserMessage = new ArrayList<>();
        searchViewLayoutUserMessage = findViewById(R.id.searchViewLayoutUserMessage);
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
                listUserMessagesAdapter.getFilter().filter(newText);
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
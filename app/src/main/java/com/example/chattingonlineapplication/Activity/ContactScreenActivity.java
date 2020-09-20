package com.example.chattingonlineapplication.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListContactAdapter;
import com.example.chattingonlineapplication.Models.Item.ContactItem;
import com.example.chattingonlineapplication.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ContactScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerContacts;
    private MaterialSearchView searchContactViewLayout;
    private List<ContactItem> lstContactItem;
    private Toolbar toolbarContact;
    private LinearLayout layoutInviteFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_screen_activity);
        reflection();
        bindingData();

        setSupportActionBar(toolbarContact);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarContact.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        layoutInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListContactAdapter adapter = new ListContactAdapter(this, lstContactItem);
        recyclerContacts.setLayoutManager(linearLayoutManager);
        recyclerContacts.setAdapter(adapter);

    }

    private void reflection() {
        lstContactItem = new ArrayList<>();
        toolbarContact = findViewById(R.id.toolbarContact);
        layoutInviteFriends = findViewById(R.id.layoutInviteFriends);
        searchContactViewLayout = findViewById(R.id.searchContactViewLayout);
        recyclerContacts = findViewById(R.id.recyclerContacts);
    }

    private void bindingData() {
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
        lstContactItem.add(new ContactItem("htttp:", "Quan Trong Tu", new Timestamp(System.currentTimeMillis()).getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_functionality_menu, menu);
        executeSearch(menu);
        return true;
    }

    private void executeSearch(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.itemSearch);
        searchContactViewLayout.setMenuItem(menuItem);
        searchContactViewLayout.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
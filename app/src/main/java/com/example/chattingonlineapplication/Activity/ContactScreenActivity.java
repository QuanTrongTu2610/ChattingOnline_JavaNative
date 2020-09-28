package com.example.chattingonlineapplication.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListContactAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Database.SQLite.ContactSQLiteHelper;
import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.Item.ContactItem;
import com.example.chattingonlineapplication.Models.PhoneContact;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.w3c.dom.Document;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ContactScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerContacts;
    private MaterialSearchView searchContactViewLayout;
    private Toolbar toolbarContact;
    private LinearLayout layoutInviteFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_screen_activity);
        reflection();

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

        new LoadingContact().execute();

    }

    private void reflection() {
        toolbarContact = findViewById(R.id.toolbarContact);
        layoutInviteFriends = findViewById(R.id.layoutInviteFriends);
        searchContactViewLayout = findViewById(R.id.searchContactViewLayout);
        recyclerContacts = findViewById(R.id.recyclerContacts);
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

    class LoadingContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //getPhoneContact:
            ContactSQLiteHelper c = new ContactSQLiteHelper(ContactScreenActivity.this);
            final List<PhoneContact> contactsPhoneNumbers = c.getAll();
            try {
                UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                userDao.getAll().continueWith(
                        new Continuation<QuerySnapshot, Object>() {
                            @Override
                            public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                List<User> friends = new ArrayList<>();
                                List<DocumentSnapshot> lst = task.getResult().getDocuments();
                                HashMap<String, User> phoneFromFirestore = new HashMap<>();
                                for (int i = 0; i < lst.size(); i++) {
                                    User user = lst.get(i).toObject(User.class);
                                    phoneFromFirestore.put(user.getUserPhoneNumber(), user);
                                    Log.i("User Friend", "Vào đây để add" + user.getUserPhoneNumber());
                                    Log.i("User Friend", "Vào đây để add" + user.getUserPhoneNumber());
                                }

                                Log.i("User Friend", "123123123d" + phoneFromFirestore.get("+84948705933").getUserLastName());
                                List<ContactItem> lstContactItem = new ArrayList<>();
                                ContactItem contactItem = new ContactItem();
                                for (int i = 0; i < contactsPhoneNumbers.size(); i++) {
                                    try {
                                        User user = phoneFromFirestore.get(contactsPhoneNumbers.get(i).getPhoneNumber());
                                        if (user != null) {
                                            contactItem.setUserAvatarUrl(user.getUserAvatarUrl());
                                            contactItem.setUserName(user.getUserFirstName()  + " " + user.getUserLastName());
                                            contactItem.setLastMessageAt(0);
                                            lstContactItem.add(contactItem);
                                        }
                                    } catch (Exception e) {
                                    }
                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ContactScreenActivity.this);
                                ListContactAdapter adapter = new ListContactAdapter(ContactScreenActivity.this, lstContactItem);
                                recyclerContacts.setLayoutManager(linearLayoutManager);
                                recyclerContacts.setAdapter(adapter);
                                return null;
                            }
                        }
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
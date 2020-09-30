package com.example.chattingonlineapplication.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListContactAdapter;
import com.example.chattingonlineapplication.Database.FireStore.ContactDao;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.InstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.SQLite.ContactSQLiteHelper;
import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.PhoneContact;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.List;

public class ContactScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerContacts;
    private MaterialSearchView searchContactViewLayout;
    private Toolbar toolbarContact;
    private LinearLayout layoutInviteFriends;
    private ProgressBar progressContactLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);
        reflection();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerContacts.setLayoutManager(linearLayoutManager);

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
        progressContactLoader = findViewById(R.id.progressContactLoader);
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

    class LoadingContact extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            //getPhoneContact:
            ContactSQLiteHelper c = new ContactSQLiteHelper(ContactScreenActivity.this);
            List<PhoneContact> fromContact = c.getAll();
            //Check two AND update Contact
            for (int i = 0; i < fromContact.size(); i++) {
                PhoneContact phoneContact = fromContact.get(i);
                try {
                    //getUser
                    FireStoreOpenConnection
                            .getInstance()
                            .getAccessToFireStore()
                            .collection(InstanceDataBaseProvider.userCollection)
                            .whereEqualTo("userPhoneNumber", phoneContact.getPhoneNumber())
                            .get()
                            //Get User From DataBase
                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                @Override
                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                    List<DocumentSnapshot> document = task.getResult().getDocuments();
                                    User user2 = document.get(0).toObject(User.class);
                                    return user2;
                                }
                            })
                            .continueWith(new Continuation<Object, Object>() {
                                @Override
                                public Object then(@NonNull Task<Object> task) throws Exception {
                                    final User user2 = (User) task.getResult();
                                    FireStoreOpenConnection
                                            .getInstance()
                                            .getAccessToFireStore()
                                            .collection(InstanceDataBaseProvider.contactCollection)
                                            .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .get()
                                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                                @Override
                                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                                    List<Contact> listQuerySnap = task.getResult().toObjects(Contact.class);
                                                    if (listQuerySnap.size() == 0) {
                                                        return false;
                                                    }
                                                    for (int j = 0; j < listQuerySnap.size(); j++) {
                                                        if (listQuerySnap.get(j).getUserFriend().getUserId().equals(user2.getUserId())) {
                                                            return true;
                                                        }
                                                    }
                                                    return false;
                                                }
                                            })
                                            .continueWith(new Continuation<Object, Object>() {
                                                @Override
                                                public Object then(@NonNull Task<Object> task) throws Exception {
                                                    if (!(boolean) task.getResult()) {
                                                        createContact(user2);
                                                    }
                                                    return null;
                                                }
                                            })
                                            .continueWith(new Continuation<Object, Object>() {
                                                @Override
                                                public Object then(@NonNull Task<Object> task) throws Exception {
                                                    FireStoreOpenConnection
                                                            .getInstance()
                                                            .getAccessToFireStore()
                                                            .collection(InstanceDataBaseProvider.contactCollection)
                                                            .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .get()
                                                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                                                @Override
                                                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                                                    progressContactLoader.setVisibility(View.GONE);
                                                                    List<Contact> contacts = task.getResult().toObjects(Contact.class);
                                                                    ListContactAdapter adapter = new ListContactAdapter(ContactScreenActivity.this, contacts);
                                                                    recyclerContacts.setAdapter(adapter);
                                                                    return null;
                                                                }
                                                            });
                                                    return null;
                                                }
                                            });
                                    return null;
                                }
                            })
                    ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    private String autoGenId() throws IOException {
        return FireStoreOpenConnection.getInstance().getAccessToFireStore()
                .collection("contact")
                .document()
                .getId();
    }

    private void createContact(User user) throws Exception {
        Contact contact = new Contact();
        contact.setContactId(autoGenId());
        contact.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        contact.setUserFriend(user);
        ContactDao contactDao = new ContactDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
        contactDao.create(contact);
    }
}
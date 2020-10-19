package com.example.chattingonlineapplication.Activity;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chattingonlineapplication.Adapter.ListContactAdapter;
import com.example.chattingonlineapplication.Database.FireStore.ContactDao;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Database.SQLite.ContactSQLiteHelper;
import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.Item.ContactItem;
import com.example.chattingonlineapplication.Models.PhoneContact;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactScreenActivity extends AppCompatActivity {

    private List<ContactItem> contactItems = new ArrayList<>();
    private RecyclerView recyclerContacts;
    private MaterialSearchView searchContactViewLayout;
    private Toolbar toolbarContact;
    private LinearLayout layoutInviteFriends;
    private ProgressBar progressContactLoader;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);
        reflection();

        //setup recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListContactAdapter adapter = new ListContactAdapter(this, contactItems);
        recyclerContacts.setAdapter(adapter);
        recyclerContacts.setLayoutManager(linearLayoutManager);

        //initialize
        initializeAdapter();

        //Refresh
        refreshList();

        //setup ToolBar
        setSupportActionBar(toolbarContact);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarContact.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Invite Friends
        layoutInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void refreshList() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    updateContact();
                    swipeRefresh.setRefreshing(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.clLightBlue, getTheme()));
    }

    private void reflection() {
        toolbarContact = findViewById(R.id.toolbarContact);
        layoutInviteFriends = findViewById(R.id.layoutInviteFriends);
        searchContactViewLayout = findViewById(R.id.searchContactViewLayout);
        recyclerContacts = findViewById(R.id.recyclerContacts);
        progressContactLoader = findViewById(R.id.progressContactLoader);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_functionality_menu, menu);
        executeSearch(menu);
        return true;
    }

    //search
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

    //update recent Contact
    private void updateContact() throws Exception {
        ContactSQLiteHelper c = new ContactSQLiteHelper(ContactScreenActivity.this);
        List<PhoneContact> fromContact = c.getAll();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Log.i("size", "fromContact" + fromContact.size());

        final UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
        //For each contact from phone's data
        for (int i = 0; i < fromContact.size(); i++) {
            final PhoneContact phoneContact = fromContact.get(i);
            //Provide single thread
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        userDao.get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        //get author User
                                        final User contactUser = documentSnapshot.toObject(User.class);
                                        try {
                                            FireStoreOpenConnection
                                                    .getInstance()
                                                    .getAccessToFireStore()
                                                    .collection(IInstanceDataBaseProvider.userCollection)
                                                    .whereEqualTo("userPhoneNumber", phoneContact.getPhoneNumber())
                                                    .get()
                                                    //Get User From DataBase
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            List<DocumentSnapshot> document = queryDocumentSnapshots.getDocuments();
                                                            if (!document.isEmpty()) {
                                                                //getConnectedUser
                                                                final User connectedUser = document.get(0).toObject(User.class);
                                                                try {
                                                                    //Check If Contact exist
                                                                    FireStoreOpenConnection
                                                                            .getInstance()
                                                                            .getAccessToFireStore()
                                                                            .collection(IInstanceDataBaseProvider.contactCollection)
                                                                            .whereEqualTo("contactUserId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                            .get()
                                                                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                                                                @Override
                                                                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                                                                    List<Contact> listQuerySnap = task.getResult().toObjects(Contact.class);
                                                                                    if (listQuerySnap.size() == 0) {
                                                                                        return false;
                                                                                    }
                                                                                    for (int j = 0; j < listQuerySnap.size(); j++) {
                                                                                        if (listQuerySnap.get(j).getConnectedUserId().equals(connectedUser.getUserId())) {
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
                                                                                        Contact contact = createContact(contactUser.getUserId(), connectedUser.getUserId());
                                                                                        updateAdapter(contact.getContactId(), contactUser, connectedUser, contact.getConversationId());
                                                                                    }
                                                                                    return null;
                                                                                }
                                                                            });
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }
                                                    });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }

    //update item
    private void updateAdapter(String contactId, User contactUser, User connectedUser, String conversationId) throws Exception {
        contactItems.add(new ContactItem(contactId, contactUser, connectedUser, conversationId));
        recyclerContacts.getAdapter().notifyDataSetChanged();
    }

    private String autoGenId(String name) throws IOException {
        return FireStoreOpenConnection.getInstance().getAccessToFireStore()
                .collection(name)
                .document()
                .getId();
    }

    private Contact createContact(String contactUserId, String connectedUserId) throws Exception {
        Contact contact = new Contact();
        contact.setContactId(autoGenId(IInstanceDataBaseProvider.contactCollection));
        contact.setContactUserId(contactUserId);
        contact.setConnectedUserId(connectedUserId);
        contact.setConversationId(autoGenId(IInstanceDataBaseProvider.conversationCollection));
        ContactDao contactDao = new ContactDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
        contactDao.create(contact);
        return contact;
    }

    //initialize and load contact
    private void initializeAdapter() {
        try {
            final UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
            userDao.get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .continueWith(new Continuation<DocumentSnapshot, Object>() {
                        @Override
                        public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                            //get ContactUser
                            final User contactUser = task.getResult().toObject(User.class);

                            //getContact
                            FireStoreOpenConnection
                                    .getInstance()
                                    .getAccessToFireStore()
                                    .collection(IInstanceDataBaseProvider.contactCollection)
                                    .whereEqualTo("contactUserId", contactUser.getUserId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            final List<Contact> contacts = queryDocumentSnapshots.toObjects(Contact.class);
                                            Log.i("Size", contacts.size() + " ");
                                            for (int i = 0; i < contacts.size(); i++) {
                                                final String userId = contacts.get(i).getConnectedUserId();
                                                final String contactId = contacts.get(i).getContactId();
                                                final String conversationId = contacts.get(i).getConversationId();
                                                try {
                                                    userDao.get(userId)
                                                            .continueWith(new Continuation<DocumentSnapshot, Object>() {
                                                                @Override
                                                                public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                                                    User connectedUser = task.getResult().toObject(User.class);
                                                                    if (connectedUser != null) {
                                                                        contactItems.add(new ContactItem(contactId, contactUser, connectedUser, conversationId));
                                                                        recyclerContacts.getAdapter().notifyDataSetChanged();
                                                                    }
                                                                    return null;
                                                                }
                                                            });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
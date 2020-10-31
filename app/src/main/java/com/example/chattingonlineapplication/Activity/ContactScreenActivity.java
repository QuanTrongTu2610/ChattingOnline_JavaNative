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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ContactScreenActivity extends AppCompatActivity {


    private static final String TAG = ContactScreenActivity.class.getSimpleName();
    private List<ContactItem> contactItems;
    private RecyclerView recyclerContacts;
    private MaterialSearchView searchContactViewLayout;
    private Toolbar toolbarContact;
    private LinearLayout layoutAddNewContact;
    private ProgressBar progressContactLoader;
    private FloatingActionButton btnAddNewFriend;
    private UserDao userDao;
    private ContactDao contactDao;

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
        layoutAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        btnAddNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeListContact();
    }

    private void reflection() {
        contactItems = new ArrayList<>();
        toolbarContact = findViewById(R.id.toolbarContact);
        layoutAddNewContact = findViewById(R.id.layoutAddNewContact);
        searchContactViewLayout = findViewById(R.id.searchContactViewLayout);
        recyclerContacts = findViewById(R.id.recyclerContacts);
        progressContactLoader = findViewById(R.id.progressContactLoader);
        btnAddNewFriend = findViewById(R.id.btnAddNewFriend);
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

    private String autoGenId(String name) {
        try {
            return FireStoreOpenConnection.getInstance().getAccessToFireStore()
                    .collection(name)
                    .document()
                    .getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //initialize and load exist Contact
    private void initializeListContact() {
        if (contactItems.size() > 0) {
            contactItems.removeAll(contactItems);
            recyclerContacts.getAdapter().notifyDataSetChanged();
        }
        try {
            userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
            userDao.get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .continueWith(new Continuation<DocumentSnapshot, Object>() {
                        @Override
                        public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                            //get owner
                            User owner = task.getResult().toObject(User.class);
                            //getExistContact from database
                            FireStoreOpenConnection
                                    .getInstance()
                                    .getAccessToFireStore()
                                    .collection(IInstanceDataBaseProvider.contactCollection)
                                    .whereArrayContains("participants", owner.getUserId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<Contact> contacts = queryDocumentSnapshots.toObjects(Contact.class);
                                            //Contacts from cloud database
                                            if (contacts.size() > 0) {
                                                for (int i = 0; i < contacts.size(); i++) {
                                                    Contact contact = contacts.get(i);
                                                    String connectedUserId = contact
                                                            .getParticipants()
                                                            .stream()
                                                            .filter(str -> !str.equals(owner.getUserId()))
                                                            .collect(Collectors.toList()).get(0);
                                                    if (connectedUserId != null || !connectedUserId.isEmpty()) {
                                                        try {
                                                            userDao.get(connectedUserId)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            User connectedUser = documentSnapshot.toObject(User.class);
                                                                            if (connectedUser != null) {
                                                                                ContactItem item = new ContactItem(
                                                                                        contact.getContactId(),
                                                                                        owner, connectedUser,
                                                                                        contact.getConversationId()
                                                                                );
                                                                                if (!contactItems.contains(item)) {
                                                                                    contactItems.add(item);
                                                                                    recyclerContacts.getAdapter().notifyItemInserted(contactItems.size());
                                                                                }
                                                                            }
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    });
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                            new UpdatingContactFromLocalToCloud().execute();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                            return null;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Check Contact in Local Contact
    //If it is not exist will create Contact
    private void updateContactFromLocal(User owner) {
        try {
            ContactSQLiteHelper c = new ContactSQLiteHelper(ContactScreenActivity.this);
            List<PhoneContact> fromContact = c.getAll();
            for (int i = 0; i < fromContact.size(); i++) {
                PhoneContact p = fromContact.get(i);
                FireStoreOpenConnection
                        .getInstance()
                        .getAccessToFireStore()
                        .collection(IInstanceDataBaseProvider.userCollection)
                        .whereEqualTo("userPhoneNumber", p.getPhoneNumber())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots != null && queryDocumentSnapshots.toObjects(User.class).size() > 0) {
                                    User connectedUser = queryDocumentSnapshots.toObjects(User.class).get(0);
                                    Log.i(TAG, "connectedUser: " + connectedUser.getUserFirstName());
                                    checkIfContactExist(owner, connectedUser);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkIfContactExist(User owner, User connectedUser) {
        try {
            ArrayList<String> arrayList = sortString(new ArrayList<>(Arrays.asList(connectedUser.getUserId(), FirebaseAuth.getInstance().getCurrentUser().getUid())));
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.contactCollection)
                    .whereEqualTo("participants", arrayList)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.i("Size............................", queryDocumentSnapshots.toObjects(Contact.class).size() + "");
                            if (queryDocumentSnapshots != null && queryDocumentSnapshots.toObjects(Contact.class).size() == 0) {
                                Contact contact = new Contact(
                                        autoGenId(IInstanceDataBaseProvider.contactCollection),
                                        autoGenId(IInstanceDataBaseProvider.conversationCollection),
                                        arrayList
                                );
                                createContact(contact, owner, connectedUser);
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createContact(Contact contact, User owner, User connectedUser) {
        try {
            contactDao = new ContactDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
            contactDao.create(contact)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "Contact: " + "Create Contact Success");
                            if (!contactItems.contains(contact)) {
                                contactItems.add(new ContactItem(contact.getContactId(), owner, connectedUser, contact.getConversationId()));
                                recyclerContacts.getAdapter().notifyItemInserted(contactItems.size());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sortPaticipants
    private ArrayList<String> sortString(ArrayList<String> a) {
        ArrayList<String> arrayList = new ArrayList<>(a);
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).compareTo(arrayList.get(j)) > 0) {
                    String temp = arrayList.get(j - 1);
                    arrayList.set(j - 1, arrayList.get(j));
                    arrayList.set(j, temp);
                }
            }
        }
        return arrayList;
    }

    class UpdatingContactFromLocalToCloud extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                FireStoreOpenConnection
                        .getInstance()
                        .getAccessToFireStore()
                        .collection(IInstanceDataBaseProvider.userCollection)
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot != null) {
                                    updateContactFromLocal(documentSnapshot.toObject(User.class));
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
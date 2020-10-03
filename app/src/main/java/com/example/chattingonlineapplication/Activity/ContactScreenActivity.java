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
import com.example.chattingonlineapplication.Database.FireStore.Interface.InstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Database.SQLite.ContactSQLiteHelper;
import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.PhoneContact;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        //UpdateContact
        new contactExecution().execute();
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

    class contactExecution extends AsyncTask<Void, Void, ExecutorService> {

        @Override
        protected ExecutorService doInBackground(Void... voids) {
            ExecutorService ex = Executors.newSingleThreadExecutor();
            try {
                ex.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                            userDao.get(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid())
                                    .continueWith(new Continuation<DocumentSnapshot, Object>() {
                                        @Override
                                        public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                            ContactSQLiteHelper c = new ContactSQLiteHelper(ContactScreenActivity.this);
                                            List<PhoneContact> fromContact = c.getAll();
                                            ExecutorService executorService = Executors.newSingleThreadExecutor();

                                            final User contactUser = task.getResult().toObject(User.class);

                                            for (int i = 0; i < fromContact.size(); i++) {
                                                final PhoneContact phoneContact = fromContact.get(i);
                                                executorService.execute(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
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
                                                                            if (!document.isEmpty()) {
                                                                                final User connectedUser = document.get(0).toObject(User.class);
                                                                                try {
                                                                                    FireStoreOpenConnection
                                                                                            .getInstance()
                                                                                            .getAccessToFireStore()
                                                                                            .collection(InstanceDataBaseProvider.contactCollection)
                                                                                            .whereEqualTo("contactUser", contactUser)
                                                                                            .get()
                                                                                            .continueWith(new Continuation<QuerySnapshot, Object>() {
                                                                                                @Override
                                                                                                public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                                                                                    List<Contact> listQuerySnap = task.getResult().toObjects(Contact.class);
                                                                                                    if (listQuerySnap.size() == 0) {
                                                                                                        return false;
                                                                                                    }
                                                                                                    for (int j = 0; j < listQuerySnap.size(); j++) {
                                                                                                        if (listQuerySnap.get(j).getConnectedUser().getUserId().equals(connectedUser.getUserId())) {
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
                                                                                                        createContact(contactUser, connectedUser);
                                                                                                    }
                                                                                                    return null;
                                                                                                }
                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    });
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            return null;
                                                                        }
                                                                    });
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                            executorService.shutdown();
                                            return null;
                                        }
                                    });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            return ex;
        }

        @Override
        protected void onPostExecute(ExecutorService executorService) {
            super.onPostExecute(executorService);
            executorService.shutdown();

            try {
                UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                userDao.get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .continueWith(new Continuation<DocumentSnapshot, Object>() {
                            @Override
                            public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                User contactUser = task.getResult().toObject(User.class);
                                FireStoreOpenConnection
                                        .getInstance()
                                        .getAccessToFireStore()
                                        .collection(InstanceDataBaseProvider.contactCollection)
                                        .whereEqualTo("contactUser", contactUser)
                                        .get()
                                        .continueWith(new Continuation<QuerySnapshot, Object>() {
                                            @Override
                                            public Object then(@NonNull Task<QuerySnapshot> task) throws Exception {
                                                progressContactLoader.setVisibility(View.GONE);
                                                //Use notifyDataSetInsert
                                                List<Contact> contacts = task.getResult().toObjects(Contact.class);
                                                ListContactAdapter adapter = new ListContactAdapter(ContactScreenActivity.this, contacts);
                                                recyclerContacts.setAdapter(adapter);
                                                return null;
                                            }
                                        });
                                return null;
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String autoGenId() throws IOException {
            return FireStoreOpenConnection.getInstance().getAccessToFireStore()
                    .collection(InstanceDataBaseProvider.contactCollection)
                    .document()
                    .getId();
        }

        private void createContact(User contactUser, User connectedUser) throws Exception {
            Contact contact = new Contact();
            contact.setContactId(autoGenId());
            contact.setContactUser(contactUser);
            contact.setConnectedUser(connectedUser);
            ContactDao contactDao = new ContactDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
            contactDao.create(contact);
        }
    }
}
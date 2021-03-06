package com.example.chattingonlineapplication.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chattingonlineapplication.Adapter.ListConversationsAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Database.SQLite.ContactSQLiteHelper;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Models.Item.ConversationItem;
import com.example.chattingonlineapplication.Models.PhoneContact;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = HomeScreenActivity.class.getSimpleName();
    private ArrayList<ConversationItem> items;
    private View viewHeader;
    private LinearLayoutManager linearLayoutManager;
    private ListConversationsAdapter listConversationsAdapter;
    private MaterialSearchView searchViewLayoutUserMessage;
    private FloatingActionButton flbtnNewMessage;
    private RecyclerView recyclerConversation;
    private Toolbar toolbarHomeScreen;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final int PERMISSION_READCONTACT = 300;
    //InDrawer
    private CircleImageView imgUserAvatar;
    private TextView tvNameOfUser;
    private TextView tvUserPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        reflection();

        //Setup tool bar
        setUpToolBar();
        setUpConversationListView();

        //Request Contact Permission
        requestContactPermission();

        //Navigate to Contact Screen
        flbtnNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreenActivity.this, ContactScreenActivity.class);
                startActivity(intent);
            }
        });

        //Navigate to Manage Profile
        if (imgUserAvatar != null) {
            imgUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        //Menu Drawer View
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemNewGroup:
                        Intent intentGroup = new Intent(HomeScreenActivity.this, ChatRoomActivity.class);
                        startActivity(intentGroup);
                        break;
                    case R.id.itemContacts:
                        Intent intentContact = new Intent(HomeScreenActivity.this, ContactScreenActivity.class);
                        startActivity(intentContact);
                        break;
                    case R.id.itemCalls:
                        break;
                    case R.id.itemSaveMessage:
                        break;
                    case R.id.itemSettings:
                        break;
                    case R.id.itemAppFAQ:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateConversationToList();
    }

    private void updateConversationToList() {
        if (items.size() > 0) {
            items.removeAll(items);
            recyclerConversation.getAdapter().notifyDataSetChanged();
        }
        try {
            new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore())
                    .get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            if (!user.getUserAvatarUrl().isEmpty()) {
                                Picasso.get().load(user.getUserAvatarUrl()).into(imgUserAvatar);
                            }
                            tvNameOfUser.setText(user.getUserFirstName() + " " + user.getUserLastName());
                            tvUserPhoneNumber.setText(user.getUserPhoneNumber());
                            try {
                                getConversation(user);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reflection() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbarHomeScreen = findViewById(R.id.toolbarHomeScreen);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbarHomeScreen, R.string.open, R.string.close);
        recyclerConversation = findViewById(R.id.recyclerConversation);
        searchViewLayoutUserMessage = findViewById(R.id.searchViewLayoutUserMessage);
        viewHeader = navigationView.getHeaderView(0);
        flbtnNewMessage = findViewById(R.id.flbtnNewMessage);
        imgUserAvatar = viewHeader.findViewById(R.id.imgUserAvatar);
        tvNameOfUser = viewHeader.findViewById(R.id.tvNameOfUser);
        tvUserPhoneNumber = viewHeader.findViewById(R.id.tvUserPhoneNumber);
        items = new ArrayList<>();
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

    private void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {android.Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_READCONTACT);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.READ_CONTACTS},
                            PERMISSION_READCONTACT);
                }
            } else {
                //When permission was granted
                new ContactInitialize().execute();
            }
        } else {
            new ContactInitialize().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_READCONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new ContactInitialize().execute();
                }
                break;
        }
    }


    //Get Contact From Local phone
    class ContactInitialize extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ContactSQLiteHelper c = new ContactSQLiteHelper(HomeScreenActivity.this);
            //delete database
            c.deleteAll();
            //pass all phone book to cursor
            List<String> contactsPhoneNumber = new ArrayList<>();
            PhoneContact ph = new PhoneContact();
            //pass all phone book to cursor
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String getMobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String newMobile = getMobile
                        .replace(" ", "")
                        .trim()
                        .replaceAll("[^a-zA-Z0-9\\s+]", "");

                StringBuilder finalMobile = new StringBuilder();
                if (!newMobile.contains("+")) {
                    finalMobile.append("+");
                }
                finalMobile.append(newMobile);

                String uPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                if (!finalMobile.toString().equalsIgnoreCase(uPhone)) {
                    Log.i(TAG, "phone: " + finalMobile.toString());
                    ph.setUserName(name);
                    ph.setPhoneNumber(finalMobile.toString());
                    c.add(ph);
                }
            }
            return null;
        }
    }


    public void getConversation(User owner) throws IOException {
        String ownerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FireStoreOpenConnection
                .getInstance()
                .getAccessToFireStore()
                .collection(IInstanceDataBaseProvider.conversationCollection)
                .whereArrayContains("participants", ownerId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Conversation> cs = queryDocumentSnapshots.toObjects(Conversation.class);
                        if (cs != null) {
                            for (int i = 0; i < cs.size(); i++) {
                                Conversation conversation = cs.get(i);
                                if (conversation.getMessages().size() != 0) {
                                    String connectedId = conversation.getParticipants()
                                            .stream()
                                            .filter(str -> !str.equals(ownerId)).collect(Collectors.toList())
                                            .get(0);
                                    try {
                                        FireStoreOpenConnection
                                                .getInstance()
                                                .getAccessToFireStore()
                                                .collection(IInstanceDataBaseProvider.userCollection)
                                                .document(connectedId)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        User connectedUser = documentSnapshot.toObject(User.class);
                                                        Log.i(TAG, "ConnectedUser: " + connectedUser.getUserFirstName());
                                                        ConversationItem conversationItem = new ConversationItem(
                                                                conversation.getConversationId(),
                                                                owner,
                                                                connectedUser,
                                                                conversation.getMessages().get(conversation.getMessages().size() - 1)
                                                        );
                                                        //Update View
                                                        items.add(conversationItem);
                                                        recyclerConversation.getAdapter().notifyDataSetChanged();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    public void setUpToolBar() {
        setSupportActionBar(toolbarHomeScreen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_black_24);
    }

    public void setUpConversationListView() {
        linearLayoutManager = new LinearLayoutManager(this);
        ListConversationsAdapter adapter = new ListConversationsAdapter(this, items);
        recyclerConversation.setLayoutManager(linearLayoutManager);
        recyclerConversation.setAdapter(adapter);
    }
}
package com.example.chattingonlineapplication.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Adapter.ListAllUserAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Item.UserItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class AddUserToContactScreenActivity extends AppCompatActivity {

    private RelativeLayout toolBarContainerNewContact;
    private Toolbar toolbarNewContact;
    private MaterialSearchView searchNewContactViewLayout;
    private ProgressBar progressNewContactLoader;
    private RecyclerView recyclerNewContacts;
    private List<UserItem> lstUser;
    private ListAllUserAdapter lstListAllUserAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_screen);
        reflection();
        //setUp toolbar
        setupToolBar();
        setupListUser();
    }

    private void reflection() {
        toolBarContainerNewContact = findViewById(R.id.toolBarContainerNewContact);
        toolbarNewContact = findViewById(R.id.toolbarNewContact);
        searchNewContactViewLayout = findViewById(R.id.searchNewContactViewLayout);
        progressNewContactLoader = findViewById(R.id.progressNewContactLoader);
        recyclerNewContacts = findViewById(R.id.recyclerNewContacts);
        lstUser = new ArrayList<>();
    }

    private void setupToolBar() {
        try {
            setSupportActionBar(toolbarNewContact);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbarNewContact.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListUser() {
        try {
            lstListAllUserAdapter = new ListAllUserAdapter(this, lstUser);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerNewContacts.setAdapter(lstListAllUserAdapter);
            recyclerNewContacts.setLayoutManager(linearLayoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu to
        getMenuInflater().inflate(R.menu.search_functionality_menu, menu);
        executeSearch(menu);
        return true;
    }

    //
    private void executeSearch(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.itemSearch);
        searchNewContactViewLayout.setMenuItem(menuItem);

        searchNewContactViewLayout.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                // load list again
            }
        });
        searchNewContactViewLayout.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // execute search
                try {
                    lstListAllUserAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // update list view
        getUserListFromFirestore();
    }

    private void getUserListFromFirestore() {
        UserItem userItem;
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.userCollection)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            try {
                                List<User> user = queryDocumentSnapshots.toObjects(User.class);
                                for (int i = 0; i < user.size(); i++) {
                                    User u = user.get(i);
                                    if (!u.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                        lstUser.add(new UserItem(
                                                u.getUserId(),
                                                u.getUserFirstName(),
                                                u.getUserLastName(),
                                                u.getUserPhoneNumber(),
                                                u.getUserAvatarUrl(),
                                                u.getUserBio(),
                                                u.getUserDateUpdated(),
                                                u.getUserDateCreated(),
                                                u.isUserIsActive(),
                                                u.getUserIpAddress(),
                                                u.getUserPort()
                                        ));
                                        recyclerNewContacts.getAdapter().notifyItemInserted(lstUser.size() - 1);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
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

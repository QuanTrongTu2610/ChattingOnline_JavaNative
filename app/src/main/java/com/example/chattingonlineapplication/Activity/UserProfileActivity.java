package com.example.chattingonlineapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chattingonlineapplication.Adapter.ListSettingOptionsUserProfileAdapter;
import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.Item.SettingUserProfileItemModel;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.CompressImage;
import com.example.chattingonlineapplication.Plugins.Interface.ICompressImageFirebase;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private static final int GALLERY_ACCESS = 303;
    private boolean isOpenCam;
    private Bitmap img;
    private List<SettingUserProfileItemModel> lst;
    private AppBarLayout appBarLayout;
    private Toolbar toolBarUserProfile;
    private RecyclerView recyclerProfileUser;
    private CollapsingToolbarLayout collapsingToolBar;
    private ImageView imgUserAvatar;
    private TextView tvUserPhoneNumber;
    private TextView tvUserBio;
    private FloatingActionButton fabChangeAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        reflection();
        bindingData();

        fabChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setTitle("Option Upload Image")
                        .setMessage("You can choose which source of image to upload")
                        .setPositiveButton("Taking a photo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (gallery.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(gallery, GALLERY_ACCESS);
                                }
                                isOpenCam = true;
                            }
                        })
                        .setNegativeButton("Open Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent gallery = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                gallery.setType("image/*");
                                gallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(gallery, GALLERY_ACCESS);
                                isOpenCam = false;
                            }
                        }).show();
            }
        });

        try {
            new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore())
                    .get(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            User user = documentSnapshot.toObject(User.class);
                            Picasso.get().load(user.getUserAvatarUrl()).into(imgUserAvatar);
                            collapsingToolBar.setTitle(user.getUserFirstName() + " " + user.getUserLastName());
                            tvUserPhoneNumber.setText(user.getUserPhoneNumber());
                            tvUserBio.setText(user.getUserBio());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


        setSupportActionBar(toolBarUserProfile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBarUserProfile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListSettingOptionsUserProfileAdapter adapter = new ListSettingOptionsUserProfileAdapter(this, lst);
        recyclerProfileUser.setLayoutManager(linearLayoutManager);
        recyclerProfileUser.setAdapter(adapter);
    }

    private void bindingData() {
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_notifications_24, "Notifications"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_lock_24, "Privacy and Security"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_bar_chart_24, "Data and Storage"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_message_24, "Chat Settings"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_folder_24, "Folders"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_devices_24, "Devices"));
        lst.add(new SettingUserProfileItemModel(R.drawable.ic_outline_language_24, "Language"));
    }

    private void reflection() {
        toolBarUserProfile = findViewById(R.id.toolBarUserProfile);
        recyclerProfileUser = findViewById(R.id.recyclerProfileUser);
        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolBar = findViewById(R.id.collapsingToolBar);
        lst = new ArrayList<>();
        imgUserAvatar = findViewById(R.id.imgUserAvatar);
        tvUserPhoneNumber = findViewById(R.id.tvUserPhoneNumber);
        tvUserBio = findViewById(R.id.tvUserBio);
        fabChangeAvatar = findViewById(R.id.fabChangeAvatar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemLogout:
                Intent intent = new Intent(UserProfileActivity.this, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACCESS) {
            if (resultCode == RESULT_OK && isOpenCam) {
                if (data != null) {
                    try {
                        img = (Bitmap) data.getExtras().get("data");
                        imgUserAvatar.setImageBitmap(img);
                        updateUserImage(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (data != null) {
                    try {
                        Uri image = data.getData();
                        img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
                        imgUserAvatar.setImageBitmap(img);
                        updateUserImage(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    private void updateUserImage(Bitmap bitmap) {
        CompressImage
                .getInstance().
                compressImageToFireBase(FirebaseAuth.getInstance().getCurrentUser().getUid(), bitmap, new ICompressImageFirebase<Uri>() {
                    @Override
                    public void compress(Uri uri) throws IOException {
                        FireStoreOpenConnection
                                .getInstance()
                                .getAccessToFireStore()
                                .collection(IInstanceDataBaseProvider.userCollection)
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .update("userAvatarUrl", uri.toString())
                                .addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Log.i("Update Url", "Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("Update Url", "Fail");
                                    }
                                });
                    }
                });
    }
}
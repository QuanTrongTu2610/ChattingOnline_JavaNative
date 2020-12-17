package com.example.chattingonlineapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.GroupChat;
import com.example.chattingonlineapplication.Utils.CompressImage;
import com.example.chattingonlineapplication.Utils.IConvertGroupToHashMap;
import com.example.chattingonlineapplication.Utils.Interface.ICompressImageFirebase;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterGroupActivity extends AppCompatActivity {

    private final static int GALLERY_ACCESS = 999;
    private boolean isOpenCam;
    private CircleImageView imgGroupAvatar;
    private EditText edtGroupTitle;
    private EditText edtGroupId;
    private Bitmap img;
    private ImageView imgCamera;
    private Toolbar toolbarRegisterGroup;

    private void reflection() {
        imgGroupAvatar = findViewById(R.id.imgGroupAvatar);
        edtGroupTitle = findViewById(R.id.edtGroupTitle);
        edtGroupId = findViewById(R.id.edtGroupId);
        imgCamera = findViewById(R.id.imgCamera);
        toolbarRegisterGroup = findViewById(R.id.toolbarRegisterGroup);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_group_chat);
        reflection();

        setupToolBar();

        //
        edtGroupTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    edtGroupTitle.setError("Empty");
                } else {
                    edtGroupTitle.setError(null);
                }
            }
        });
        edtGroupId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    edtGroupId.setError("Empty");
                } else {
                    edtGroupId.setError(null);
                }
            }
        });
        imgGroupAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(RegisterGroupActivity.this)
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
    }

    private void setupToolBar() {
        setSupportActionBar(toolbarRegisterGroup);
        toolbarRegisterGroup.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case GALLERY_ACCESS:
                    if (resultCode == RESULT_OK && isOpenCam) {
                        imgCamera.setVisibility(View.GONE);
                        img = (Bitmap) data.getExtras().get("data");
                        imgGroupAvatar.setImageBitmap(img);
                    } else {
                        Uri image = data.getData();
                        try {
                            imgCamera.setVisibility(View.GONE);
                            img = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                            imgGroupAvatar.setImageBitmap(img);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_basic_infor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneItem:
                String groupTitle = edtGroupTitle.getText().toString().trim();
                String groupId = edtGroupId.getText().toString().trim();
                if (!groupId.isEmpty() && !groupTitle.isEmpty()) {
                    if (img == null) {
                        GroupChat groupChat = new GroupChat(
                                groupId,
                                groupTitle,
                                "",
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                new ArrayList<String>()
                        );
                        createGroupChat(groupChat);
                    } else {
                        CompressImage.getInstance().compressImageToFireBase(groupId, img, "GroupImage", new ICompressImageFirebase<Uri>() {
                            @Override
                            public void compress(Uri uri) throws IOException {
                                GroupChat groupChat = new GroupChat(
                                        groupId,
                                        groupTitle,
                                        uri.toString(),
                                        FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        new ArrayList<String>()
                                );
                                createGroupChat(groupChat);
                            }
                        });
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createGroupChat(GroupChat groupChat) {
        try {
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(IInstanceDataBaseProvider.groupCollection)
                    .document(groupChat.getGroupId())
                    .set(IConvertGroupToHashMap.getInstance().convert(groupChat))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("GroupChat:", "Create Fail");
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            addToRealtimeDatabase(groupChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //finish Activity
        finish();
    }

    private void addToRealtimeDatabase(GroupChat groupChat) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference
                .child("GroupChat")
                .child(groupChat.getGroupId())
                .setValue(groupChat)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("UpdateRealTime", "Successfully.....");
                    }
                })
        ;
    }
}

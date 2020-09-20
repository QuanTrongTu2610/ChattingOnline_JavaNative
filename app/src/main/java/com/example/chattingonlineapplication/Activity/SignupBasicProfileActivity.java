package com.example.chattingonlineapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupBasicProfileActivity extends AppCompatActivity {

    private static final int GALLERY_ACCESS = 201;


    private FirebaseUser firebaseUser;
    private ImageView imgCamera;
    private CircleImageView imgUserAvatar;
    private EditText edtFirstName;
    private EditText edtLastName;
    private Uri image;
    private Toolbar toolbarRegisterInf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_basic_profile);
        reflection();

        setSupportActionBar(toolbarRegisterInf);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbarRegisterInf.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupBasicProfileActivity.this, LauncherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        edtFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    edtFirstName.setError("Empty");
                } else {
                    edtFirstName.setError(null);
                }
            }
        });
        edtLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    edtLastName.setError("Empty");
                } else {
                    edtLastName.setError(null);
                }
            }
        });

        imgUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery, GALLERY_ACCESS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACCESS) {
            image = data.getData();
            try {
                imgCamera.setVisibility(View.GONE);
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                imgUserAvatar.setImageBitmap(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reflection() {
        imgUserAvatar = findViewById(R.id.imgUserAvatar);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        imgCamera = findViewById(R.id.imgCamera);
        toolbarRegisterInf = findViewById(R.id.toolbarRegisterInf);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.register_basic_infor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.doneItem:
                String userFirstName = edtFirstName.getText().toString().trim();
                String userLastName = edtLastName.getText().toString().trim();
                if (!userFirstName.isEmpty() && !userLastName.isEmpty()) {
                    try {
                        final UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                        userDao.get(firebaseUser.getUid()).continueWith(new Continuation<DocumentSnapshot, Object>() {
                            @Override
                            public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                User user = task.getResult().toObject(User.class);

                                return null;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }

                break;
        }
        return true;
    }
}
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
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupBasicProfileActivity extends AppCompatActivity {

    private static final int GALLERY_ACCESS = 201;

    private Bitmap img;
    private boolean isOpenCam;
    private FirebaseUser firebaseUser;
    private ImageView imgCamera;
    private CircleImageView imgUserAvatar;
    private EditText edtFirstName;
    private EditText edtLastName;
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
                finish();
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
                new AlertDialog.Builder(SignupBasicProfileActivity.this)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACCESS) {
            if (resultCode == RESULT_OK && isOpenCam) {
                imgCamera.setVisibility(View.GONE);
                img = (Bitmap) data.getExtras().get("data");
                imgUserAvatar.setImageBitmap(img);
            } else {
                Uri image = data.getData();
                try {
                    imgCamera.setVisibility(View.GONE);
                    img = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                    imgUserAvatar.setImageBitmap(img);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                final String userFirstName = edtFirstName.getText().toString().trim();
                final String userLastName = edtLastName.getText().toString().trim();
                if (!userFirstName.isEmpty() && !userLastName.isEmpty()) {
                    try {
                        final UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final StorageReference reference = FirebaseStorage.getInstance()
                                .getReference()
                                .child("profileImages")
                                .child(uid + ".jpeg");

                        reference.putBytes(byteArrayOutputStream.toByteArray())
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        getDownloadUrl(reference);
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
                } else {
                    edtLastName.setError("Empty");
                    edtFirstName.setError("Empty");
                }
                break;
        }
        return true;
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("hello" , "uri" + uri);
            }
        });
    }
}
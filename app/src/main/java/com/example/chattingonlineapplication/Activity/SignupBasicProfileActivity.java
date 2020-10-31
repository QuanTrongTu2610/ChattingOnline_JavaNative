package com.example.chattingonlineapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
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
import com.example.chattingonlineapplication.Plugins.CompressImage;
import com.example.chattingonlineapplication.Plugins.Interface.ICompressImageFirebase;
import com.example.chattingonlineapplication.Plugins.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Timestamp;

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
    private AlertDialog alertDialog;

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
        if (data != null) {
            if (requestCode == GALLERY_ACCESS) {
                if (resultCode == RESULT_OK && isOpenCam) {
                    try {
                        imgCamera.setVisibility(View.GONE);
                        img = (Bitmap) data.getExtras().get("data");
                        imgUserAvatar.setImageBitmap(img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Uri image = data.getData();
                        imgCamera.setVisibility(View.GONE);
                        img = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                        imgUserAvatar.setImageBitmap(img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                alertDialog = LoadingDialog.getInstance().getDialog(this);
                alertDialog.show();
                final String userFirstName = edtFirstName.getText().toString().trim();
                final String userLastName = edtLastName.getText().toString().trim();
                if (!userFirstName.isEmpty() && !userLastName.isEmpty()) {
                    try {
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        final String ipAddress = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        final int port = generatePort();
                        if (img == null) {
                            User user = new User(
                                    uid,
                                    userFirstName,
                                    userLastName,
                                    FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                                    "",
                                    "none",
                                    0,
                                    new Timestamp(System.currentTimeMillis()).getTime(),
                                    true,
                                    ipAddress,
                                    port
                            );
                            createUser(user);
                        } else {
                            CompressImage.getInstance().compressImageToFireBase(uid, img, "profileImages", new ICompressImageFirebase<Uri>() {
                                @Override
                                public void compress(Uri uri) {
                                    User user = new User(
                                            uid,
                                            userFirstName,
                                            userLastName,
                                            FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                                            uri.toString(),
                                            "none",
                                            0,
                                            new Timestamp(System.currentTimeMillis()).getTime(),
                                            true,
                                            ipAddress,
                                            port
                                    );
                                    createUser(user);
                                }
                            });
                        }
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

    private int generatePort() throws Exception {
        ServerSocket s = new ServerSocket(0);
        int port = s.getLocalPort();
        s.close();
        return port;
    }

    private void createUser(User user) {
        try {
            UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
            userDao.create(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    alertDialog.dismiss();
                    LoadingDialog.getInstance().getDialog(SignupBasicProfileActivity.this).dismiss();
                    Intent intent = new Intent(SignupBasicProfileActivity.this, HomeScreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
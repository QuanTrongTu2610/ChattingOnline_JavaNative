package com.example.chattingonlineapplication.Activity;

import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.InstanceProvider;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.sql.Timestamp;

public class VerifyAuthCodeActivity extends AppCompatActivity {

    private EditText editTextOTP;


    private boolean isVerifiedSuccess = false;
    private String codeSent;
    private FirebaseAuth mAuth;
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_auth_code);
        reflection();

        editTextOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() == 6) {
                    verifySignInCode();
                }
            }
        });
    }

    private void verifySignInCode() {
        String otpCode = editTextOTP.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void reflection() {
        editTextOTP = findViewById(R.id.editTextOTP);
        phoneNumber = getIntent().getStringExtra("signUpPhoneNumber") != null ? getIntent().getStringExtra("signUpPhoneNumber") : "";
        mAuth = FirebaseAuth.getInstance();
        try {
            codeSent = getIntent().getStringExtra("CODE_BACK").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            new AlertDialog.Builder(VerifyAuthCodeActivity.this)
                                    .setTitle("Chatting Online App")
                                    .setMessage("Invalid OTP code")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                            ;
                        } else {
                            try {
                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                                User user = new User(firebaseUser.getUid(),
                                        "People",
                                        "A",
                                        firebaseUser.getPhoneNumber(),
                                        "None",
                                        "None",
                                        0,
                                        timestamp.getTime(),
                                        true,
                                        ip,
                                        InstanceProvider.port);
                                UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                                userDao.create(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("Result", "Success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("Result", "Fail");
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

}
package com.example.chattingonlineapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
                            Toast.makeText(VerifyAuthCodeActivity.this, "success", Toast.LENGTH_SHORT).show();
                            try {
                                UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());
                                User user = new User("asdasdas2",
                                        "Tu",
                                        "Quan Trong",
                                        "Quan Trong Tu",
                                        "+84832677917",
                                        "httll",
                                        "hello",
                                        "Time",
                                        "Time",
                                        true);
                                userDao.createUser(user);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}
package com.example.chattingonlineapplication.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chattingonlineapplication.Plugins.ProgressButton;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyAuthCodeActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private CardView cardView;
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
        sendVerificationRequest();

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
                    if (!verifySignInCode()) {
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
                    }
                }
            }
        });
    }


    private void sendVerificationRequest() {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            e.printStackTrace();
                            Toast.makeText(VerifyAuthCodeActivity.this, "Request OTP failed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            codeSent = s;
                            mResendToken = forceResendingToken;
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean verifySignInCode() {
        String otpCode = editTextOTP.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, otpCode);
        return signInWithPhoneAuthCredential(credential);
    }

    private void reflection() {
        editTextOTP = findViewById(R.id.editTextOTP);
        phoneNumber = getIntent().getStringExtra("signUpPhoneNumber") != null ? getIntent().getStringExtra("signUpPhoneNumber") : "";
        mAuth = FirebaseAuth.getInstance();
        cardView = findViewById(R.id.cardView);
        constraintLayout = findViewById(R.id.constraintLayout);
    }


    private boolean signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            isVerifiedSuccess = true;
                        }
                    }
                });
        return isVerifiedSuccess;
    }

}
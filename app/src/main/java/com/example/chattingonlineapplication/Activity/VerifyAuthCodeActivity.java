package com.example.chattingonlineapplication.Activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Database.FireStore.UserDao;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.LoadingDialog;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

public class VerifyAuthCodeActivity extends AppCompatActivity {

    private EditText editTextOTP;
    private AlertDialog alertDialog;
    private String codeSent;
    private FirebaseAuth mAuth;
    private String phoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onResume() {
        super.onResume();
        LoadingDialog.getInstance().getDialog(VerifyAuthCodeActivity.this).dismiss();
        Log.i("Resume", "Running");
    }

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
                    alertDialog = LoadingDialog.getInstance().getDialog(VerifyAuthCodeActivity.this);
                    alertDialog.show();
                    editTextOTP.setEnabled(false);
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
                                            alertDialog.dismiss();
                                        }
                                    }).show();
                            ;
                        } else {
                            try {
                                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                final UserDao userDao = new UserDao(FireStoreOpenConnection.getInstance().getAccessToFireStore());

                                userDao.get(firebaseUser.getUid()).continueWith(new Continuation<DocumentSnapshot, Object>() {
                                    @Override
                                    public Object then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                                        User user = task.getResult().toObject(User.class);
                                        if (user == null) {
                                            //Basic Infor
                                            Intent intent = new Intent(VerifyAuthCodeActivity.this, SignupBasicProfileActivity.class);
                                            startActivity(intent);
                                        } else {
                                            //To the main Screen but should update the Ip address.
                                            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                                            String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                                            FireStoreOpenConnection.getInstance().getAccessToFireStore()
                                                    .collection(IInstanceDataBaseProvider.userCollection)
                                                    .document(user.getUserId())
                                                    .update("userIpAddress", ip)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent intent = new Intent(VerifyAuthCodeActivity.this, HomeScreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            alertDialog.dismiss();
                                                            editTextOTP.setEnabled(true);
                                                            startActivity(intent);
                                                        }
                                                    });
                                        }
                                        return null;
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
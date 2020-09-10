package com.example.chattingonlineapplication.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    //FireBase
    private FirebaseAuth mAuth;

    //normal variables
    private static final int RESULT_COUNTRY_CODE = 200;
    private static final int GET_COUNTRY_CODE = 100;
    private ArrayList<CountryModel> listCountry;
    private CountryModel countryModel;
    //binding variables
    private FloatingActionButton btnRegister;
    private EditText autvPhoneCode;
    private EditText autvPhoneNumber;
    private EditText autvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        reflection();

        try {
            listCountry = (ArrayList<CountryModel>) getIntent().getExtras().getSerializable("LIST_COUNTRY");
        } catch (Exception e) {
            e.printStackTrace();
        }
        autvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, CountryListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("COUNTRY_LIST", listCountry);
                intent.putExtras(bundle);
                startActivityForResult(intent, GET_COUNTRY_CODE);
            }
        });
        autvPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().isEmpty()) {
                    autvPhoneNumber.setError("Empty");
                } else {
                    autvPhoneNumber.setError(null);
                    if (validatedPhoneCode(autvPhoneCode.getText().toString().trim())) {
                        btnRegister.setEnabled(true);
                    } else {
                        btnRegister.setEnabled(false);
                    }
                }
                if (count != 0 && before == 0) {
                    String value = charSequence.toString();
                    String first = "";
                    String second = "";
                    String third = "";
                    if (value != null && value.length() > 0) {
                        value = value.replaceAll(" ", "");
                        if (value.length() >= 3) {
                            first = value.substring(0, 3);
                        } else if (value.length() < 3) {
                            first = value.substring(0, value.length());
                        }
                        if (value.length() >= 6) {
                            second = value.substring(3, 6);
                            third = value.substring(6, value.length());
                        } else if (value.length() < 6 && value.length() > 3) {
                            second = value.substring(3, value.length());
                        }

                        StringBuffer stringBuffer = new StringBuffer();
                        if (first != null && first.length() > 0) {
                            stringBuffer.append(first);
                        }
                        if (second != null && second.length() > 0) {
                            stringBuffer.append(" ");
                            stringBuffer.append(second);
                        }
                        if (third != null && third.length() > 0) {
                            stringBuffer.append(" ");
                            stringBuffer.append(third);
                        }

                        autvPhoneNumber.removeTextChangedListener(this);
                        autvPhoneNumber.setText(stringBuffer.toString());
                        if (start == 3 || start == 7) {
                            start = start + 2;
                        } else {
                            start = start + 1;
                        }

                        if (start <= autvPhoneNumber.getText().toString().length()) {
                            autvPhoneNumber.setSelection(start);
                        } else {
                            autvPhoneNumber.setSelection(autvPhoneNumber.getText().toString().length());
                        }
                        autvPhoneNumber.addTextChangedListener(this);
                    } else {
                        autvPhoneNumber.removeTextChangedListener(this);
                        autvPhoneNumber.setText("");
                        autvPhoneNumber.addTextChangedListener(this);
                    }
                } else {
                    //delete
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        autvPhoneCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() <= 1) {
                    autvPhoneCode.setError("Empty");
                } else {
                    autvPhoneCode.setError(null);
                    if (validatedPhoneNumber(autvPhoneNumber.getText().toString().trim())) {
                        btnRegister.setEnabled(true);
                    } else {
                        btnRegister.setEnabled(false);
                    }
                }
                try {
                    countryModel = findCountryModel(Integer.valueOf(charSequence.toString().trim()));
                    autvCountry.setText(countryModel.getName().trim());
                } catch (Exception e) {
                    autvCountry.setText("Choose the country");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().contains("+")) {
                    autvPhoneCode.setText("+");
                    Selection.setSelection(autvPhoneCode.getText(), autvPhoneCode.getText().length());
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorizeAccount();
            }
        });

    }

    public boolean authorizeAccount() {
        //nothing
        String userPhoneCode = autvPhoneCode.getText().toString().trim();
        String userPhoneNumber = autvPhoneNumber.getText().toString().trim();


        if (validatedPhoneCode(userPhoneCode) && validatedPhoneNumber(userPhoneNumber)) {
            //resgister in firebase
            String finalPhoneNumber = userPhoneCode + userPhoneNumber.replaceAll(" ", "");
            Intent intent = new Intent(SignUpActivity.this, VerifyAuthCodeActivity.class);
            intent.putExtra("signUpPhoneNumber", finalPhoneNumber);
            intent.putExtra("codePhone", userPhoneCode);
            startActivity(intent);
        }
        return false;
    }

    private boolean validatedPhoneCode(String countryCode) {
        if (countryCode.length() > 1) {
            return true;
        }
        return false;
    }

    private boolean validatedPhoneNumber(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            return true;
        }
        return false;
    }

    private void reflection() {
        autvCountry = findViewById(R.id.autvCountry);
        autvPhoneNumber = findViewById(R.id.autvPhoneNumber);
        listCountry = new ArrayList<>();
        autvPhoneCode = findViewById(R.id.autvPhoneCode);
        btnRegister = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_COUNTRY_CODE) {
            if (resultCode == RESULT_COUNTRY_CODE && data.getExtras() != null) {
                CountryModel countryModel = (CountryModel) data.getExtras().getSerializable("countryObject");
                if (countryModel != null) {
                    autvCountry.setText(countryModel.getName());
                    autvPhoneCode.setText("+" + countryModel.getCallingCodes()[0]);
                }
            }
        }
    }

    protected CountryModel findCountryModel(int requestCallingCode) {
        try {
            for (CountryModel item : listCountry) {
                if (requestCallingCode == Integer.valueOf(item.getCallingCodes()[0].trim().replace(" ", ""))) {
                    return item;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
package com.example.chattingonlineapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Webservice.Input.CountryInformationInput;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.example.chattingonlineapplication.Webservice.Output.BaseOutput;
import com.example.chattingonlineapplication.Webservice.Provider.ServiceProvider;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import bolts.Continuation;
import bolts.Task;

public class LauncherActivity extends AppCompatActivity {

    private ProgressBar progressSignUp;
    private ArrayList<CountryModel> listCountry;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        reflection();
        try {
            CountryInformationInput countryInformationInput = new CountryInformationInput();
            ServiceProvider.getInstance().getCountryClient().getCountryInformation(countryInformationInput).continueWith(new Continuation<BaseOutput<CountryModel[]>, Object>() {
                @Override
                public Object then(Task<BaseOutput<CountryModel[]>> task) throws Exception {
                    BaseOutput<CountryModel[]> result = task.getResult();
                    CountryModel[] r = result.getData();
                    bindingData(r);
                    navigateScreen();
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void navigateScreen() {
        try {
            Intent intent = new Intent(LauncherActivity.this, SignUpActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("LIST_COUNTRY", listCountry);
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindingData(CountryModel[] list) {
        try {
            final Iterator<CountryModel> iterator = Arrays.asList(list).iterator();
            while (iterator.hasNext()) {
                final CountryModel countryModel = iterator.next();
                String callingCode = countryModel.getCallingCodes()[0].replace(" ", "").trim();
                if (!callingCode.isEmpty()) {
                    listCountry.add(countryModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reflection() {
        progressSignUp = findViewById(R.id.progressSignUp);
        listCountry = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
    }

//    protected class LoadingUserProfile extends AsyncTaskLoader<User> {
//        public LoadingUserProfile(@NonNull Context context) {
//            super(context);
//        }
//        @Nullable
//        @Override
//        public User loadInBackground() {
//            try {
//                Intent intent = new Intent(LauncherActivity.this, HomeScreenActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
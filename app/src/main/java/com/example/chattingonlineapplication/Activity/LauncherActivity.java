package com.example.chattingonlineapplication.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Webservice.Input.CountryInformationInput;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;
import com.example.chattingonlineapplication.Webservice.Output.BaseOutput;
import com.example.chattingonlineapplication.Webservice.Provider.ServiceProvider;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import bolts.Continuation;
import bolts.Task;

public class LauncherActivity extends AppCompatActivity {

    private final static String Tag = LauncherActivity.class.getSimpleName();
    private ProgressBar progressSignUp;
    private ArrayList<CountryModel> listCountry;
    private FirebaseAuth mAuth;
    private static final String CHANNEL_ID = "com.example.chattingonlineapplication.groupchat.participant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        reflection();
        try {
            Log.i(Tag, "Install Provider Needed");
            ProviderInstaller.installIfNeeded(getApplicationContext());

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        while (true) {
            if (isNetworkAvailable()) {
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
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //create Notification Channel
        createNotificationChannel();
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

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String nameChannel = "Group members change notification";
            String descriptionChannel = "This use to notify group chat members is changed";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, nameChannel, importance);
            channel.setDescription(descriptionChannel);
            channel.enableVibration(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
package com.example.chattingonlineapplication.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestFireBase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fire_base);

        Message message = new Message("eqweqweq",
                "1",
                "2",
                "quan trong tu oi",
                213123123,
                123213123
        );
        Map<String, Message> h = new HashMap<String, Message>();
        h.put("213123123", message);
        h.put("12321321312", message);

        try {
            Conversation conversation = new Conversation(
                    "sadjasdjlkqje",
                    "Conver1",
                    new ArrayList<String>(Arrays.asList("sadasd", "123123")),
                    h
            );

//            FireStoreOpenConnection
//                    .getInstance()
//                    .getAccessToFireStore()
//                    .collection("conversation")
//                    .add(conversation)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.i("vafo ", "sass");
//                        }
//                    });

            List<String> a = new ArrayList<String>(Arrays.asList("sadasd", "sdasdasd"));
            FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection("conversation")
                    .whereArrayContains("participants","sadasd")
                    .whereArrayContains("participants","sdasdasd")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.i("vafo ", queryDocumentSnapshots.toObjects(Conversation.class).size() + "");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
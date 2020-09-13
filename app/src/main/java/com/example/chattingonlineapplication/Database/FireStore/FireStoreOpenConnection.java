package com.example.chattingonlineapplication.Database.FireStore;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class FireStoreOpenConnection {
    private FirebaseFirestore firebaseFirestore;

    //classInstance
    private static FireStoreOpenConnection instance;

    private FireStoreOpenConnection() throws IOException {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public static FireStoreOpenConnection getInstance() throws IOException {
        if (instance == null) {
            return new FireStoreOpenConnection();
        }
        return instance;
    }

    public FirebaseFirestore getAccessToFireStore() {
        return firebaseFirestore;
    }
}

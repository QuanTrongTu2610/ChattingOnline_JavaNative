package com.example.chattingonlineapplication.Database.FireStore;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chattingonlineapplication.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UserDao {
    private static final String collectionName = "user";
    private FirebaseFirestore db;

    public UserDao(FirebaseFirestore firestore) {
        this.db = firestore;
    }

    public void createUser(User user) {
        try {
            HashMap<String, Object> container = new HashMap<>();
            container.put("userId", user.getUserId());
            container.put("userFirstName", user.getUserName());
            container.put("userLastName", user.getUserLastName());
            container.put("userName", user.getUserName());
            container.put("userBio", user.getUserBio());
            container.put("userPhoneNumber", user.getUserPhoneNumber());
            container.put("userAvatarUrl", user.getUserAvatarUrl());
            container.put("userDateUpdated", user.getUserDateUpdated());
            container.put("userDateCreated", user.getUserDateCreated());
            container.put("userIsActive", user.getUserIsActive());

            db.collection(collectionName).document(user.getUserId()).set(container)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("i", "Create success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("i", "Create fail");
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String userId) {
        try {
            db.collection(collectionName).document(userId).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("i", "Delete success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("i", "Delete fail");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

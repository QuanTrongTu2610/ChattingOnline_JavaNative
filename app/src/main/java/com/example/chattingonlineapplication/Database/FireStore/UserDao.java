package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.ConvertUserToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDao implements IObjectDao<User> {
    private User user;
    private FirebaseFirestore db;

    public UserDao(FirebaseFirestore firestore) {
        this.db = firestore;
    }


    public Task<QuerySnapshot> getAll() throws Exception {
        return db.collection(InstanceDataBaseProvider.userCollection).get();
    }

    public Task<Void> create(User user) throws Exception {
        return db.collection(InstanceDataBaseProvider.userCollection).document(user.getUserId()).set(ConvertUserToHashMap.getInstance().convert(user));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.userCollection).document(id).delete();
    }

    public Task<Void> update(User user) throws Exception {
        return db.collection(InstanceDataBaseProvider.userCollection).document(user.getUserId()).update(ConvertUserToHashMap.getInstance().convert(user));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.userCollection).document(id).get();
    }
}
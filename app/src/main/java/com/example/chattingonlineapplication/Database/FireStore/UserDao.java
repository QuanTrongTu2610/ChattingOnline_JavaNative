package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Database.FireStore.Interface.IObjectDao;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Plugins.IConvertUserToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDao implements IObjectDao<User> {
    private FirebaseFirestore db;

    public UserDao(FirebaseFirestore firestore) {
        this.db = firestore;
    }


    public Task<QuerySnapshot> getAll() throws Exception {
        return db.collection(IInstanceDataBaseProvider.userCollection).get();
    }

    public Task<Void> create(User user) throws Exception {
        return db.collection(IInstanceDataBaseProvider.userCollection).document(user.getUserId()).set(IConvertUserToHashMap.getInstance().convert(user));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.userCollection).document(id).delete();
    }

    public Task<Void> update(User user) throws Exception {
        return db.collection(IInstanceDataBaseProvider.userCollection).document(user.getUserId()).update(IConvertUserToHashMap.getInstance().convert(user));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.userCollection).document(id).get();
    }
}
package com.example.chattingonlineapplication.Database.FireStore;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public interface IObjectDao <T> {
    public Task<Void> create(T t) throws Exception;

    public Task<Void> delete(String id) throws Exception;

    public Task<Void> update(T t) throws Exception;

    public Task<DocumentSnapshot> get(String id) throws Exception;
}

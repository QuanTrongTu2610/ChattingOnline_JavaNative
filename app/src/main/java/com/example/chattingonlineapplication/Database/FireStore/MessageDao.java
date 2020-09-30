package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Plugins.ConvertMessageToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessageDao implements IObjectDao<Message> {
    private Message message;
    private FirebaseFirestore db;

    public MessageDao(FirebaseFirestore firestore) {
        this.db = firestore;
    }

    public Task<Void> create(Message message) throws Exception {
        return db.collection(InstanceDataBaseProvider.messageCollection).document(message.getMessageId()).set(ConvertMessageToHashMap.getInstance().convert(message));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.messageCollection).document(id).delete();
    }

    public Task<Void> update(Message message) throws Exception {
        return db.collection(InstanceDataBaseProvider.messageCollection).document(message.getMessageId()).set(ConvertMessageToHashMap.getInstance().convert(message));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.messageCollection).document(id).get();
    }
}

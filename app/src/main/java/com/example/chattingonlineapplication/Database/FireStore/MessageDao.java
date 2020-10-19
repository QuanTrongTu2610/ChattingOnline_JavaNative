package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Database.FireStore.Interface.IObjectDao;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.Plugins.IConvertMessageToHashMap;
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
        return db.collection(IInstanceDataBaseProvider.messageCollection).document(message.getMessageId()).set(IConvertMessageToHashMap.getInstance().convert(message));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.messageCollection).document(id).delete();
    }

    public Task<Void> update(Message message) throws Exception {
        return db.collection(IInstanceDataBaseProvider.messageCollection).document(message.getMessageId()).set(IConvertMessageToHashMap.getInstance().convert(message));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.messageCollection).document(id).get();
    }
}

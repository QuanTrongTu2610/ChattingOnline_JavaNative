package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Database.FireStore.Interface.IObjectDao;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Utils.IConvertConversationToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConversationDao implements IObjectDao<Conversation> {
    private FirebaseFirestore db;

    public ConversationDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public Task<Void> create(Conversation conversation) throws Exception {
        return db.collection(IInstanceDataBaseProvider.conversationCollection).document(conversation.getConversationId()).set(IConvertConversationToHashMap.getInstance().convert(conversation));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.conversationCollection).document(id).delete();
    }

    public Task<Void> update(Conversation conversation) throws Exception {
        return db.collection(IInstanceDataBaseProvider.conversationCollection).document(conversation.getConversationId()).set(IConvertConversationToHashMap.getInstance().convert(conversation));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(IInstanceDataBaseProvider.conversationCollection).document(id).get();
    }
}

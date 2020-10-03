package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Database.FireStore.Interface.IObjectDao;
import com.example.chattingonlineapplication.Database.FireStore.Interface.InstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.Plugins.ConvertConversationToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConversationDao implements IObjectDao<Conversation> {
    private Conversation conversation;
    private FirebaseFirestore db;

    @Override
    public Task<Void> create(Conversation conversation) throws Exception {
        return db.collection(InstanceDataBaseProvider.conversationCollection).document(conversation.getConversationId()).set(ConvertConversationToHashMap.getInstance().convert(conversation));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.conversationCollection).document(id).delete();
    }

    public Task<Void> update(Conversation conversation) throws Exception {
        return db.collection(InstanceDataBaseProvider.conversationCollection).document(conversation.getConversationId()).set(ConvertConversationToHashMap.getInstance().convert(conversation));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.conversationCollection).document(id).get();
    }
}

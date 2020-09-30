package com.example.chattingonlineapplication.Database.FireStore;

import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Plugins.ConvertContactToHashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContactDao implements IObjectDao<Contact> {
    private Contact contact;
    private FirebaseFirestore db;

    public ContactDao(FirebaseFirestore firestore) {
        this.db = firestore;
    }

    public Task<Void> create(Contact contact) throws Exception {
        return db.collection(InstanceDataBaseProvider.contactCollection).document(contact.getContactId()).set(ConvertContactToHashMap.getInstance().convert(contact));
    }

    public Task<Void> delete(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.contactCollection).document(id).delete();
    }

    public Task<Void> update(Contact contact) throws Exception {
        return db.collection(InstanceDataBaseProvider.contactCollection).document(contact.getContactId()).set(ConvertContactToHashMap.getInstance().convert(contact));
    }

    public Task<DocumentSnapshot> get(String id) throws Exception {
        return db.collection(InstanceDataBaseProvider.contactCollection).document(id).get();
    }

}

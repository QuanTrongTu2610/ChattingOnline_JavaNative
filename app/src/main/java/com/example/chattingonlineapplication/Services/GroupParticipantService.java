package com.example.chattingonlineapplication.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.Models.Item.GroupChatItem;
import com.example.chattingonlineapplication.Models.User;
import com.example.chattingonlineapplication.Utils.InstanceProvider;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;

public class GroupParticipantService extends Service {

    private static final String TAG = GroupParticipantService.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private RealtimeDatabaseExecutor realtimeDatabaseExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service: onCreate");
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // this method will be called when annother component triggers.
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    // return to server connected
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service: onBind");
        return new GroupParticipantBinder();
    }

    // return to server unconnected
    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service: onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service: onDestroy");
        super.onDestroy();
        realtimeDatabaseExecutor.interrupt();

    }

    public void realtimeDatabaseListener(Context context, GroupChatItem groupChatItem) {
        realtimeDatabaseExecutor = new RealtimeDatabaseExecutor(context, groupChatItem);
        realtimeDatabaseExecutor.start();
    }

    // binder
    public class GroupParticipantBinder extends Binder {
        public GroupParticipantService getService() {
            return GroupParticipantService.this;
        }
    }

    // thread
    class RealtimeDatabaseExecutor extends Thread {
        private GroupChatItem groupChatItem;
        private Context context;

        public RealtimeDatabaseExecutor(Context context, GroupChatItem groupChatItem) {
            this.context = context;
            this.groupChatItem = groupChatItem;
        }

        @Override
        public void run() {
            super.run();
            databaseReference
                    .child("GroupChat")
                    .child(groupChatItem.getGroupId())
                    .child("participants")
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            // new user was added:
                            // sending broadcast
                            try {
                                if (!snapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    FireStoreOpenConnection
                                            .getInstance()
                                            .getAccessToFireStore()
                                            .collection(IInstanceDataBaseProvider.userCollection)
                                            .document(snapshot.getKey())
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot != null) {
                                                        Intent intent = new Intent();
                                                        intent.setAction("UserAddNotification");
                                                        intent.putExtra("USER_ID", snapshot.getKey());
                                                        intent.putExtra("USER", documentSnapshot.toObject(User.class));
                                                        intent.putExtra("ACTION", InstanceProvider.ADD);
                                                        LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                            // new user was deleted:
                            // sending broadcast
                            Log.i(TAG, "RealTime: " + "Someone was removed");
                            try {
                                FireStoreOpenConnection
                                        .getInstance()
                                        .getAccessToFireStore()
                                        .collection(IInstanceDataBaseProvider.userCollection)
                                        .document(snapshot.getKey())
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot != null) {
                                                    Intent intent = new Intent();
                                                    intent.setAction("UserAddNotification");
                                                    intent.putExtra("USER_ID", snapshot.getKey());
                                                    intent.putExtra("USER", documentSnapshot.toObject(User.class));
                                                    intent.putExtra("ACTION", InstanceProvider.REMOVE);
                                                    LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}

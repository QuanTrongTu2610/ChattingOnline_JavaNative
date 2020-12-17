package com.example.chattingonlineapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Database.FireStore.FireStoreOpenConnection;
import com.example.chattingonlineapplication.Database.FireStore.Interface.IInstanceDataBaseProvider;
import com.example.chattingonlineapplication.HandleEvent.IUserListClickListener;
import com.example.chattingonlineapplication.Models.Contact;
import com.example.chattingonlineapplication.Models.Item.UserItem;
import com.example.chattingonlineapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListAllUserAdapter extends RecyclerView.Adapter implements Filterable {

    private List<UserItem> lstClone;
    private List<UserItem> lstUser;
    private Context context;

    public ListAllUserAdapter(Context context, List<UserItem> lst) {
        this.context = context;
        this.lstUser = lst;
        this.lstClone = new ArrayList<>(lstUser);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserItem item = lstUser.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (!item.getUserAvatarUrl().isEmpty())
            Picasso.get().load(item.getUserAvatarUrl()).into(viewHolder.imgUserAvatar);
        viewHolder.tvUserName.setText(item.getUserFirstName() + " " + item.getUserLastName());
        viewHolder.tvUserFriendPhoneNumber.setText(item.getUserPhoneNumber());
        viewHolder.setEventClickListener(new IUserListClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    // create
                    UserItem userItem = lstUser.get(position);
                    new ContactCreating().execute(userItem);
                    try {
                        ((Activity) context).finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<UserItem> filteredList = new ArrayList<>();
                if (charSequence.length() == 0 || charSequence == null) {
                    filteredList.addAll(lstClone);
                } else {
                    for (UserItem item : lstClone) {
                        String phoneNumber = item.getUserPhoneNumber().trim();
                        if (phoneNumber.contains(charSequence)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lstUser.clear();
                lstUser.addAll((ArrayList<UserItem>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvUserFriendPhoneNumber;
        TextView tvUserName;
        CircleImageView imgUserAvatar;
        LinearLayout container;
        IUserListClickListener iUserListClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserFriendPhoneNumber = itemView.findViewById(R.id.tvUserFriendPhoneNumber);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            container = itemView.findViewById(R.id.containerUser);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        public void setEventClickListener(IUserListClickListener iUserListClickListener) {
            this.iUserListClickListener = iUserListClickListener;
        }

        @Override
        public void onClick(View view) {
            iUserListClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            iUserListClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }

    private class ContactCreating extends AsyncTask<UserItem, Void, Void> {

        @Override
        protected Void doInBackground(UserItem... items) {
            try {
                ArrayList<String> sortedString = sortString(
                        new ArrayList<String>(Arrays.asList(
                                items[0].getUserId(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid()
                        )));
                FireStoreOpenConnection
                        .getInstance()
                        .getAccessToFireStore()
                        .collection(IInstanceDataBaseProvider.contactCollection)
                        .whereEqualTo("participants", sortedString)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                try {
                                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.toObjects(Contact.class).size() == 0) {
                                        FireStoreOpenConnection
                                                .getInstance()
                                                .getAccessToFireStore()
                                                .collection(IInstanceDataBaseProvider.contactCollection)
                                                .document(autoGenId(IInstanceDataBaseProvider.contactCollection))
                                                .set(new Contact(
                                                        autoGenId(IInstanceDataBaseProvider.contactCollection),
                                                        autoGenId(IInstanceDataBaseProvider.conversationCollection),
                                                        sortedString
                                                ));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });

                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private String autoGenId(String name) {
        try {
            return FireStoreOpenConnection
                    .getInstance()
                    .getAccessToFireStore()
                    .collection(name)
                    .document()
                    .getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<String> sortString(ArrayList<String> a) {
        ArrayList<String> arrayList = new ArrayList<>(a);
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 1; j < arrayList.size(); j++) {
                if (arrayList.get(i).compareTo(arrayList.get(j)) > 0) {
                    String temp = arrayList.get(j - 1);
                    arrayList.set(j - 1, arrayList.get(j));
                    arrayList.set(j, temp);
                }
            }
        }
        return arrayList;
    }
}

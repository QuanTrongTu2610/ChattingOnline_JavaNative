package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Activity.ChattingScreenActivity;
import com.example.chattingonlineapplication.HandleEvent.IContactListClickListener;
import com.example.chattingonlineapplication.Models.Item.ContactItem;
import com.example.chattingonlineapplication.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListContactAdapter extends RecyclerView.Adapter implements Filterable {

    private List<ContactItem> lstClone;
    private List<ContactItem> lstContact;
    private Context context;

    public ListContactAdapter(Context context, List<ContactItem> lst) {
        this.context = context;
        this.lstContact = lst;
        this.lstClone = new ArrayList<>(lstContact);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactItem item = lstContact.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
//        Picasso.get().load(item.getUserAvatarUrl()).into(viewHolder.imgUserAvatar);
        viewHolder.imgUserAvatar.setImageResource(R.drawable.ava);
        viewHolder.tvUserName.setText(item.getUserName());
        viewHolder.tvLastMessageAt.setText("Last message at " + new Date(item.getLastMessageAt() * 1000));
    }

    @Override
    public int getItemCount() {
        return lstContact.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ContactItem> filteredList = new ArrayList<>();
                if (charSequence.length() == 0 || charSequence == null) {
                    filteredList.addAll(lstClone);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (ContactItem item : lstClone) {
                        if (item.getUserName().toLowerCase().contains(filter)) {
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
                lstContact.clear();
                lstContact.addAll((ArrayList<ContactItem>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView imgUserAvatar;
        private TextView tvUserName;
        private TextView tvLastMessageAt;

        private IContactListClickListener iContactListClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
            this.tvLastMessageAt = itemView.findViewById(R.id.tvLastMessageAt);
        }

        public void setEventClickListener(IContactListClickListener iContactListClickListener) {
            this.iContactListClickListener = iContactListClickListener;
        }

        @Override
        public void onClick(View view) {
            iContactListClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            iContactListClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}

package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Conversation;
import com.example.chattingonlineapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListConversationsAdapter extends RecyclerView.Adapter implements Filterable {

    private ArrayList<Conversation> lstUserMessageClone;
    private ArrayList<Conversation> lstUserMessage;
    private Context context;

    public ListConversationsAdapter(Context context, ArrayList<Conversation> lstUserMessage) {
        this.context = context;
        this.lstUserMessage = lstUserMessage;
        this.lstUserMessageClone = new ArrayList<>(lstUserMessage);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.user_message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Conversation item = lstUserMessage.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;


//        Picasso.get().load(userMessage.getUser().getUserAvatarUrl()).into(viewHolder.imgUserAvatar);

        viewHolder.imgUserAvatar.setImageResource(R.drawable.ava);
        viewHolder.tvUserName.setText(item.getcReceiverName());
        viewHolder.tvUserTimeSending.setText(item.getcDateSending());
        viewHolder.tvUserCurrentMessage.setText(item.getcLastMessage().trim());
    }

    @Override
    public int getItemCount() {
        return lstUserMessage.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Conversation> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(lstUserMessageClone);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (Conversation item : lstUserMessageClone) {
                        if (item.getcReceiverName().toLowerCase().trim().contains(filter)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lstUserMessage.clear();
                lstUserMessage.addAll((ArrayList<Conversation>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imgUserAvatar;
        public ImageView imgUserIsSeen;
        public TextView tvUserName;
        public TextView tvUserTimeSending;
        public TextView tvUserCurrentMessage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgUserAvatar = itemView.findViewById(R.id.imgUserAvatar);
            this.imgUserIsSeen = itemView.findViewById(R.id.imgUserIsSeen);
            this.tvUserCurrentMessage = itemView.findViewById(R.id.tvUserCurrentMessage);
            this.tvUserTimeSending = itemView.findViewById(R.id.tvUserTimeSending);
            this.tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }
}
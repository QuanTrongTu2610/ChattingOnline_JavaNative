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
import com.example.chattingonlineapplication.HandleEvent.IConversationListClickListener;
import com.example.chattingonlineapplication.Models.Item.ConversationItem;
import com.example.chattingonlineapplication.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListConversationsAdapter extends RecyclerView.Adapter implements Filterable {

    private ArrayList<ConversationItem> lstUserMessageClone;
    private ArrayList<ConversationItem> lstUserMessage;
    private Context context;

    public ListConversationsAdapter(Context context, ArrayList<ConversationItem> lstUserMessage) {
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
        ConversationItem item = lstUserMessage.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;


//        Picasso.get().load(userMessage.getUser().getUserAvatarUrl()).into(viewHolder.imgUserAvatar);

        viewHolder.imgUserAvatar.setImageResource(R.drawable.ava);
        viewHolder.tvUserName.setText(item.getcReceiverName());
        viewHolder.tvUserTimeSending.setText(item.getcDateSending());
        viewHolder.tvUserCurrentMessage.setText(item.getcLastMessage().trim());
        viewHolder.setiConversationListClickListener(new IConversationListClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick) {
                    Intent intent = new Intent(context, ChattingScreenActivity.class);
                    context.startActivity(intent);
                }
            }
        });
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
                ArrayList<ConversationItem> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(lstUserMessageClone);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (ConversationItem item : lstUserMessageClone) {
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
                lstUserMessage.addAll((ArrayList<ConversationItem>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        public IConversationListClickListener iConversationListClickListener;

        public View layoutUserConversation;
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
            this.layoutUserConversation = itemView.findViewById(R.id.layoutUserConversation);
            layoutUserConversation.setOnLongClickListener(this);
            layoutUserConversation.setOnClickListener(this);
        }

        public void setiConversationListClickListener(IConversationListClickListener i) {
            this.iConversationListClickListener = i;
        }

        @Override
        public void onClick(View view) {
            iConversationListClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            iConversationListClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
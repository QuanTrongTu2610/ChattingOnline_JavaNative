package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.UserMessage;
import com.example.chattingonlineapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListUserMessagesAdapter extends RecyclerView.Adapter implements Filterable {

    private ArrayList<UserMessage> lstUserMessageClone;
    private ArrayList<UserMessage> lstUserMessage;
    private Context context;

    public ListUserMessagesAdapter(Context context, ArrayList<UserMessage> lstUserMessage) {
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
        UserMessage userMessage = lstUserMessage.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (userMessage.getUser().getUserAvatarUrl().trim().isEmpty() || userMessage.getUser().getUserAvatarUrl() == null) {
            //nothing
        } else {
            Picasso.get().load(userMessage.getUser().getUserAvatarUrl()).into(viewHolder.imgUserAvatar);
        }
        viewHolder.tvUserName.setText(userMessage.getUser().getUserName());
        viewHolder.tvUserTimeSending.setText(userMessage.getUserTimeSend());
        viewHolder.tvUserCurrentMessage.setText(userMessage.getUserCurrentMessage().getText());
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
                ArrayList<UserMessage> filteredList = new ArrayList<>();
                if (charSequence == null || charSequence.length() == 0) {
                    filteredList.addAll(lstUserMessageClone);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim().replace(" ", "");
                    for (UserMessage item : lstUserMessageClone) {
                        if (item.getUser().getUserName().toLowerCase().contains(filter)) {
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
                lstUserMessage.addAll((ArrayList<UserMessage>) filterResults.values);
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
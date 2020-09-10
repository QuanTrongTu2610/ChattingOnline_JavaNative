package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.R;

import java.util.List;

public class MessageChattingApdater extends RecyclerView.Adapter {

    private Context context;
    List<Message> lstMessage;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstMessage.size();
    }

    //inner class

    protected class SendViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSelfContent;
        public TextView tvSelfMessageTime;
        public ImageView imgIsSeen;

        public SendViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSelfContent = itemView.findViewById(R.id.tvSelfContent);
            this.tvSelfMessageTime = itemView.findViewById(R.id.tvSelfMessageTime);
            this.imgIsSeen = itemView.findViewById(R.id.imgIsSeen);
        }
    }

    protected class ReceiveViewHoler extends RecyclerView.ViewHolder {
        public TextView tvSenderContent;
        public TextView tvSenderMessageTime;
        public ImageView imgSenderAvatar;
        public TextView tvSenderName;

        public ReceiveViewHoler(@NonNull View itemView) {
            super(itemView);
            this.tvSenderContent = itemView.findViewById(R.id.tvSenderContent);
            this.tvSenderMessageTime = itemView.findViewById(R.id.tvSenderMessageTime);
            this.imgSenderAvatar = itemView.findViewById(R.id.imgSenderAvatar);
            this.tvSenderName = itemView.findViewById(R.id.tvSenderName);
        }
    }
}

package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.Item.MessageItem;
import com.example.chattingonlineapplication.Models.Message;
import com.example.chattingonlineapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageAdapter extends RecyclerView.Adapter {

    private final static int MESSAGE_SENDER = 2;
    //you
    private final static int MESSAGE_RECEIVER = 1;

    private List<MessageItem> lstMessage;
    private Context context;
    private FirebaseUser firebaseUser;

    public ListMessageAdapter(Context context, List<MessageItem> lst, FirebaseUser firebaseUser) {
        this.context = context;
        this.lstMessage = lst;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == MESSAGE_SENDER) {
            viewHolder = new SenderViewHolder(inflater.inflate(R.layout.item_message_sender, parent, false));
        } else if (viewType == MESSAGE_RECEIVER) {
            viewHolder = new ReceiverViewHolder(inflater.inflate(R.layout.item_message_receiver, parent, false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageItem item = lstMessage.get(position);
        switch (holder.getItemViewType()) {
            case MESSAGE_RECEIVER:
                try {
                    ((ReceiverViewHolder) holder).bindingView(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MESSAGE_SENDER:
                Log.i("Position", position + "");
                try {
                    ((SenderViewHolder) holder).bindingView(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return lstMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (firebaseUser != null) {
            if (firebaseUser.getUid().equalsIgnoreCase(lstMessage.get(position).getUserSender().getUserId())) {
                return MESSAGE_RECEIVER;
            } else {
                return MESSAGE_SENDER;
            }
        }
        return 0;
    }


    //Receiver
    protected class ReceiverViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgIsSeen;
        public TextView tvSelfMessageTime;
        public TextView tvSelfContent;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgIsSeen = itemView.findViewById(R.id.imgIsSeen);
            this.tvSelfContent = itemView.findViewById(R.id.tvSelfContent);
            this.tvSelfMessageTime = itemView.findViewById(R.id.tvSelfMessageTime);
        }

        public void bindingView(MessageItem m) throws Exception {
            //mock
            imgIsSeen.setVisibility(View.INVISIBLE);
            tvSelfMessageTime.setText(new Date(m.getMessageDateCreated()).toString());
            tvSelfContent.setText(m.getContent());
        }
    }


    //Sender
    protected class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSenderContent;
        private TextView tvSenderMessageTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSenderContent = itemView.findViewById(R.id.tvSenderContent);
            this.tvSenderMessageTime = itemView.findViewById(R.id.tvSenderMessageTime);
        }

        public void bindingView(MessageItem m) {
            tvSenderContent.setText(m.getContent());
            tvSenderMessageTime.setText(new Date(m.getMessageDateCreated()).toString());
        }
    }
}
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
import com.example.chattingonlineapplication.Plugins.TimeConverter;
import com.example.chattingonlineapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMessageAdapter extends RecyclerView.Adapter {
    private final static int MESSAGE_SENDER = 2;
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
        //should show avatar
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
                try {
                    if (position == 0) {
                        ((SenderViewHolder) holder).bindingView(item);
                    } else {
                        Log.i("sadasd", item.getUserSender().getUserId().equals(lstMessage.get(position - 1).getUserSender().getUserId()) + "");
                        if (!item.getUserSender().getUserId().equals(lstMessage.get(position - 1).getUserSender().getUserId())) {
                            ((SenderViewHolder) holder).bindingView(item);
                        } else {
                            ((SenderViewHolder) holder).bindingViewWithoutImage(item);
                        }
                    }
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
            tvSelfMessageTime.setText(TimeConverter.getInstance().convertToMinutes(new Date(m.getMessageDateCreated())));
            tvSelfContent.setText(m.getContent());
        }
    }

    //Sender
    protected class SenderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSenderContent;
        private TextView tvSenderMessageTime;
        private CircleImageView userAvatar;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvSenderContent = itemView.findViewById(R.id.tvSenderContent);
            this.tvSenderMessageTime = itemView.findViewById(R.id.tvSenderMessageTime);
            this.userAvatar = itemView.findViewById(R.id.userAvatar);
        }

        public void bindingView(MessageItem m) {
            if (!m.getUserSender().getUserAvatarUrl().isEmpty())
                Picasso.get().load(m.getUserSender().getUserAvatarUrl()).into(userAvatar);
            tvSenderContent.setText(m.getContent());
            tvSenderMessageTime.setText(TimeConverter.getInstance().convertToMinutes(new Date(m.getMessageDateCreated())));
        }

        public void bindingViewWithoutImage(MessageItem m) {
            userAvatar.setVisibility(View.INVISIBLE);
            tvSenderContent.setText(m.getContent());
            tvSenderMessageTime.setText(TimeConverter.getInstance().convertToMinutes(new Date(m.getMessageDateCreated())));
        }
    }
}
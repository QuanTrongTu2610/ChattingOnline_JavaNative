package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Activity.GroupChatScreenActivity;
import com.example.chattingonlineapplication.HandleEvent.IGroupListClickListener;
import com.example.chattingonlineapplication.Models.Item.GroupChatItem;
import com.example.chattingonlineapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListGroupChatAdapter extends RecyclerView.Adapter {

    private ArrayList<GroupChatItem> listGroupChatItem;
    private Context context;

    public ListGroupChatAdapter(Context context, ArrayList<GroupChatItem> groupChatItems) {
        this.context = context;
        this.listGroupChatItem = groupChatItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_message, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (listGroupChatItem.size() > 0) {
            GroupChatItem item = listGroupChatItem.get(position);
            viewHolder.bindingView(item);
            viewHolder.registerOnClickEvent(new IGroupListClickListener() {
                @Override
                public void onClick(int position, boolean isLongClick) {
                    if (!isLongClick) {
                        //Navigate to chat Screen
                        Intent intent = new Intent(context, GroupChatScreenActivity.class);
                        intent.putExtra("GROUPCHAT_ITEM", item);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listGroupChatItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private LinearLayout groupChatItem;
        private IGroupListClickListener iGroupListClickListener;
        private CircleImageView imgGroupAvatar;
        private TextView tvGroupTitle;
        private TextView tvGroupId;
        private TextView tvGroupAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.groupChatItem = itemView.findViewById(R.id.groupChatItem);
            this.imgGroupAvatar = itemView.findViewById(R.id.imgGroupAvatar);
            this.tvGroupTitle = itemView.findViewById(R.id.tvGroupTitle);
            this.tvGroupId = itemView.findViewById(R.id.tvGroupId);
            this.tvGroupAuthor = itemView.findViewById(R.id.tvGroupAuthor);
            groupChatItem.setOnClickListener(this);
            groupChatItem.setOnLongClickListener(this);
        }

        public void registerOnClickEvent(IGroupListClickListener i) {
            if (iGroupListClickListener == null) {
                this.iGroupListClickListener = i;
            }
        }

        public void bindingView(GroupChatItem item) {
            if (!item.getAvatarUrl().trim().isEmpty()) {
                Picasso.get().load(item.getAvatarUrl()).into(imgGroupAvatar);
            }
            tvGroupTitle.setText(item.getTitle());
            tvGroupId.setText("Group Id: " + item.getGroupId());
            tvGroupAuthor.setText("Host by: " + item.getAuthor().getUserFirstName() + " " + item.getAuthor().getUserLastName());
        }

        @Override
        public void onClick(View v) {
            iGroupListClickListener.onClick(getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            iGroupListClickListener.onClick(getAdapterPosition(), true);
            return true;
        }
    }
}

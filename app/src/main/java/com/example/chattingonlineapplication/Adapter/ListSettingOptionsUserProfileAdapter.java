package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chattingonlineapplication.HandleEvent.IProfileSettingItemClickListener;
import com.example.chattingonlineapplication.Models.Item.SettingUserProfileItemModel;
import com.example.chattingonlineapplication.R;
import java.util.List;

public class ListSettingOptionsUserProfileAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SettingUserProfileItemModel> lstItem;

    public ListSettingOptionsUserProfileAdapter(Context context, List<SettingUserProfileItemModel> lst) {
        this.context = context;
        this.lstItem = lst;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.profile_setting_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingUserProfileItemModel model = lstItem.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvItemName.setText(model.getItemName());
        viewHolder.imgIcon.setImageResource(model.getItemIcon());
        viewHolder.setClickListener(new IProfileSettingItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick == false) {
                    Toast.makeText(context, "ccccc" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstItem.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public IProfileSettingItemClickListener iProfileSettingItemClickListener;

        public View layoutItem;
        public ImageView imgIcon;
        public TextView tvItemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            layoutItem.setOnClickListener(this);
            layoutItem.setOnLongClickListener(this);
        }

        public void setClickListener(IProfileSettingItemClickListener i) {
            this.iProfileSettingItemClickListener = i;
        }

        @Override
        public void onClick(View view) {
            iProfileSettingItemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            iProfileSettingItemClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}

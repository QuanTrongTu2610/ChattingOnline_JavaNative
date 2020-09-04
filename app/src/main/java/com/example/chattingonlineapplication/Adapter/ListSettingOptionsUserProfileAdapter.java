package com.example.chattingonlineapplication.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.Models.SettingUserProfileItemModel;

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
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lstItem.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
            
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

package com.example.chattingonlineapplication.HandleEvent;

import android.view.View;

public interface IConversationListClickListener {
    void onClick(View view, int position, boolean isLongClick);
}

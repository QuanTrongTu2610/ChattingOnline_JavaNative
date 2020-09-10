package com.example.chattingonlineapplication.Plugins;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chattingonlineapplication.R;

public class ProgressButton {

    private CardView cardView;
    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    private TextView textView;

    Animation fade_in;

    public ProgressButton(Context context, View view) {
        this.cardView = view.findViewById(R.id.cardView);
        this.constraintLayout = view.findViewById(R.id.constraintLayout);
        this.progressBar = view.findViewById(R.id.progressBar);
        this.textView = view.findViewById(R.id.textView);
    }

    public void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please wait....");
    }

    public void buttonFinished() {
        progressBar.setVisibility(View.GONE);
        textView.setText("Done");
    }
}

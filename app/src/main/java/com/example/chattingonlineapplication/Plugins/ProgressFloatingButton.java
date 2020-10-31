package com.example.chattingonlineapplication.Plugins;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.chattingonlineapplication.R;

public class ProgressFloatingButton {

    private ImageView imgBtnVerification;
    private ConstraintLayout constraintLayoutBtnVerification;
    private ProgressBar progressBarBtnVerification;

    public ProgressFloatingButton(Context context, View view) {
        this.imgBtnVerification = view.findViewById(R.id.imgBtnVerification);
        this.constraintLayoutBtnVerification = view.findViewById(R.id.constraintLayoutBtnVerification);
        this.progressBarBtnVerification = view.findViewById(R.id.progressBarBtnVerification);
    }

    public void buttonActivated() {
        progressBarBtnVerification.setVisibility(View.VISIBLE);
        imgBtnVerification.setImageResource(0);
    }

    public void buttonFinished() {
        progressBarBtnVerification.setVisibility(View.GONE);
        imgBtnVerification.setImageResource(R.drawable.ic_baseline_done_24);
    }

    public void buttonDefault() {
        progressBarBtnVerification.setVisibility(View.GONE);
        imgBtnVerification.setImageResource(R.drawable.ic_baseline_arrow_forward_24);
    }
}

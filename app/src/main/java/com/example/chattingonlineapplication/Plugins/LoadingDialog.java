package com.example.chattingonlineapplication.Plugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import com.example.chattingonlineapplication.R;

public class LoadingDialog {
    private Context context;
    private AlertDialog alertDialog;
    private static LoadingDialog instance;


    public LoadingDialog(Context context) {
        this.context = context;
    }

    public static LoadingDialog getInstance(Context context) {
        if (instance == null) {
            instance = new LoadingDialog(context);
        }
        return instance;
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.custom_progress_loader, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        return alertDialog;
    }

}

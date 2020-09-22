package com.example.chattingonlineapplication.Plugins;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.example.chattingonlineapplication.R;

public class LoadingDialog {
    private AlertDialog alertDialog;
    private static LoadingDialog instance;


    public static LoadingDialog getInstance() {
        if (instance == null) {
            instance = new LoadingDialog();
        }
        return instance;
    }

    public AlertDialog getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        builder.setView(inflater.inflate(R.layout.custom_progress_loader, null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        return alertDialog;
    }

}

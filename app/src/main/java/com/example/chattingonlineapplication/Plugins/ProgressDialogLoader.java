package com.example.chattingonlineapplication.Plugins;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogLoader {
    private static ProgressDialog dialog;
    private static ProgressDialogLoader progressDialogLoader;

    private ProgressDialogLoader (Context context) {
        dialog = new ProgressDialog(context);
    }

    public static ProgressDialog getInstance(Context context) {
        if (progressDialogLoader == null ) {
            progressDialogLoader = new ProgressDialogLoader(context);
        }
        return progressDialogLoader.dialog;
    }
}
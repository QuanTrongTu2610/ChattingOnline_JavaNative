package com.example.chattingonlineapplication.Plugins;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chattingonlineapplication.Plugins.Interface.ICompressImageFirebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CompressImage {
    private ByteArrayOutputStream byteArrayOutputStream;
    private static CompressImage instance;

    private CompressImage() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    public static CompressImage getInstance() {
        if (instance == null) {
            instance = new CompressImage();
        }
        return instance;
    }

    public void compressImageToFireBase(String id, Bitmap img,String folderName,  final ICompressImageFirebase<Uri> icompressImageFirebase) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        final StorageReference reference = FirebaseStorage.getInstance()
                .getReference()
                .child(folderName)
                .child(id + ".jpeg");

        reference.putBytes(byteArrayOutputStream.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {
                                    icompressImageFirebase.compress(uri);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}

package com.example.alpha1;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FBauth {
    public static FirebaseStorage FBST = FirebaseStorage.getInstance();
    public static StorageReference refST = FBST.getReference();
    public static StorageReference refStamp = refST.child("Stamps");
    public static StorageReference refFull = refST.child("Full");
    public static StorageReference refGallery = refST.child("Gallery");
    public static FirebaseAuth refAuth = FirebaseAuth.getInstance();
}

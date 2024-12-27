package com.example.alpha1;

import static com.example.alpha1.FBauth.refStamp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class camera extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Button btnTakePicture, btnDownloadImage;
    private ImageView imageView;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String uploadedImagePath = "images/captured_image.jpg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnDownloadImage = findViewById(R.id.btnDownloadImage);
        imageView = findViewById(R.id.imageView);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        btnTakePicture.setOnClickListener(v -> openCamera());
        btnDownloadImage.setOnClickListener(v -> downloadImage());
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImageToFirebase(imageBitmap);
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference imageRef = storageReference.child(uploadedImagePath);

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot ->
                        Toast.makeText(camera.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(camera.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void downloadImage() {
        StorageReference imageRef = storageReference.child(uploadedImagePath);

        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Load the image using any library (e.g., Glide or Picasso)
            Glide.with(camera.this)
                    .load(uri)
                    .into(imageView);
        }).addOnFailureListener(e ->
                Toast.makeText(camera.this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

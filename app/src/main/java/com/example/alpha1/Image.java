package com.example.alpha1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;


public class Image extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri imageUri;
    Button select, upload;
    ImageView IV;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        select = findViewById(R.id.selectButton);
        upload = findViewById(R.id.uploadButton);
        IV = findViewById(R.id.imageView);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Authentication");
        menu.add("camera");
        menu.add("AI");
        menu.add("Notification");
        return super.onCreateOptionsMenu(menu);
    }

    public void ImagePicker(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Authentication")){
            Intent intent =  new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (st.equals("camera")) {
            Intent intent = new Intent(this, camera.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Get the image URI
        }
    }

    public void uploadImageToFirebase(View view) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Retrieve the download URL
                        Task<Uri> downloadUriTask = taskSnapshot.getStorage().getDownloadUrl();
                        downloadUriTask.addOnSuccessListener(downloadUri -> {
                            String downloadUrl = downloadUri.toString();
                            Toast.makeText(Image.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                            displayImageFromFirebase(downloadUrl);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(Image.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayImageFromFirebase(String downloadUrl) {
        Picasso.get().load(downloadUrl).into(IV);
    }
}
package com.example.alpha1;

import static com.example.alpha1.FBauth.refAuth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText ETgmail;
    EditText ETpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        ETgmail = findViewById(R.id.editTextTextEmailAddress);
        ETpassword = findViewById(R.id.editTextTextPassword);
        };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("image");
        menu.add("camera");
        menu.add("AI");
        menu.add("Notification");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("image")){
            Intent intent =  new Intent(this, Image.class);
            startActivity(intent);
        } else if (st.equals("camera")) {
            Intent intent = new Intent(this, camera.class);
            startActivity(intent);
        } else if(st.equals("Notification")){
            Intent intent = new Intent(this, Notifications.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, AI.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void btn(View view) {
        String email=ETgmail.getText().toString();
        String password = ETpassword.getText().toString();
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("connecting");
            pd.setMessage("creating user");
            pd.show();
            refAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    if (task.isSuccessful()){
                        Log.i("MainActivity","createUserWithEmailAndPassword:success");
                        FirebaseUser user = refAuth.getCurrentUser();
                        user.getUid();
                        Toast.makeText(MainActivity.this, "user created successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

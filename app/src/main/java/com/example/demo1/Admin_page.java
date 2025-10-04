package com.example.demo1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Admin_page extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextEnroll;
    private Button buttonSave;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEnroll = findViewById(R.id.editTextenroll);
        buttonSave = findViewById(R.id.buttonLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInputAndSave();
            }
        });
    }

    private void validateInputAndSave() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String enroll = editTextEnroll.getText().toString().trim();

        if (enroll.isEmpty()) {
            editTextEnroll.setError("Enter the Enrollment Number");
            editTextEnroll.requestFocus();
            return;
        }

        // Validate inputs
        if (username.isEmpty()) {
            editTextUsername.setError("Enter the Username");
            editTextUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Enter the Password");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            editTextPassword.setError("Password must be at least 6 characters long");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length()>8){
            editTextPassword.setError("Password cannot be more than 8 characters long");
            editTextPassword.requestFocus();
            return;
        }




        // Save user data to Firebase Realtime Database
        saveUserData(enroll, username, password);
    }

    public void saveUserData(String enroll, String username, String password) {
        // Create a UserData object
            UserData userData = new UserData(enroll,username, null,null,null,null,null,null,password);

        // Save user data to Firebase Realtime Database under "students/enroll" node
        databaseReference.child("students").child(enroll).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Admin_page.this, "Data saved successfully", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), login.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(Admin_page.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

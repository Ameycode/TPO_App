package com.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText editTextEnroll, editTextPassword;
    private Button buttonLogin;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        editTextEnroll = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextEnroll.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if(username.equals("Admin")&&password.equals("12345"))
                {
                    Intent i = new Intent(login.this,AdminDashBoard.class);
                    startActivity(i);
                }
                ValidateLogin();

                if (validateLogin(username, password)) {
                    // Login successful
                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private boolean validateLogin(final String username, final String password) {
        if (username.isEmpty()) {
            editTextEnroll.setError("Enter the Username");
            editTextEnroll.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Enter the Password");
            editTextPassword.requestFocus();
            return false;
        }

        // Query Firebase to check if the username exists and retrieve its password
        databaseReference.child("students").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    UserData userData = snapshot.getValue(UserData.class);
                    if (userData != null && userData.getPass().equals(password)) {
                        // Password matches, login successful
                        Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(login.this, StudentDashboard.class);
                        intent.putExtra("username", username);
                        intent.putExtra("pass", password);
                        startActivity(intent);
                        finish();
                    } else {
                        // Password does not match
                        Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Username not found
                    Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return false; // Default return, actual login state determined in onDataChange
    }

    private void ValidateLogin() {

        String e1= editTextEnroll.getText().toString();
        String e2= editTextPassword.getText().toString();

        if (e1.isEmpty()) {
            editTextEnroll.setError("Enter the Enrollment no");
            editTextEnroll.requestFocus();
            return;
        }
        if (e1.length()<10) {
            editTextEnroll.setError("Enter the valid Enrollment no");
            editTextEnroll.requestFocus();
            return;
        }
        if (e1.length()>10) {
            editTextEnroll.setError("Enter the valid Enrollment no");
            editTextEnroll.requestFocus();
            return;
        }

        if (e2.isEmpty()) {
            editTextPassword.setError("Enter the Password");
            editTextPassword.requestFocus();
            return;
        }
        if (e2.length()<6) {
            editTextPassword.setError("Enter the valid Password");
            editTextPassword.requestFocus();
            return;
        }
        if (e2.length()>8) {
            editTextPassword.setError("Enter the valid Password");
            editTextPassword.requestFocus();
            return;
        }
    }
}

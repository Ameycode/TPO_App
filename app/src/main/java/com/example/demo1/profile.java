package com.example.demo1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAdhar, etDob, etEnrollno, etGender, etPercentage, etStatus;
    private Button btnEdit, btnSave;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAdhar = findViewById(R.id.etAdhar);
        etDob = findViewById(R.id.etDob);
        etEnrollno = findViewById(R.id.etEnrollno);
        etGender = findViewById(R.id.etGender);
        etPercentage = findViewById(R.id.etPercentage);
        etStatus = findViewById(R.id.etStatus);

        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("additional_details").child("2000150027");

        // Load profile data from Firebase
        loadProfileFromFirebase();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
                enableEditing(false);
            }
        });
    }

    private void enableEditing(boolean enabled) {
        etName.setEnabled(enabled);
        etEmail.setEnabled(enabled);
        etPhone.setEnabled(enabled);
        etAdhar.setEnabled(enabled);
        etDob.setEnabled(enabled);
        etEnrollno.setEnabled(enabled);
        etGender.setEnabled(enabled);
        etPercentage.setEnabled(enabled);
        etStatus.setEnabled(enabled);
        btnSave.setVisibility(enabled ? View.VISIBLE : View.GONE);
        btnEdit.setVisibility(enabled ? View.GONE : View.VISIBLE);
    }

    private void loadProfileFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                if (userProfile != null) {
                    etName.setText(userProfile.name);
                    etEmail.setText(userProfile.email);
                    etPhone.setText(userProfile.phone);
                    etAdhar.setText(userProfile.adhar);
                    etDob.setText(userProfile.dob);
                    etEnrollno.setText(userProfile.enrollno);
                    etGender.setText(userProfile.gender);
                    etPercentage.setText(userProfile.percentage);
                    etStatus.setText(userProfile.status);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(profile.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String adhar = etAdhar.getText().toString();
        String dob = etDob.getText().toString();
        String enrollno = etEnrollno.getText().toString();
        String gender = etGender.getText().toString();
        String percentage = etPercentage.getText().toString();
        String status = etStatus.getText().toString();

        // Save the details to database
        UserProfile userProfile = new UserProfile();
        userProfile.name = name;
        userProfile.email = email;
        userProfile.phone = phone;
        userProfile.adhar = adhar;
        userProfile.dob = dob;
        userProfile.enrollno = enrollno;
        userProfile.gender = gender;
        userProfile.percentage = percentage;
        userProfile.status = status;

        databaseReference.setValue(userProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(profile.this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(profile.this, "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

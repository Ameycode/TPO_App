package com.example.demo1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class feedback extends AppCompatActivity {

    // Define your UI components
    private EditText nameEditText;
    private EditText enrollmentNoEditText;
    private EditText mobileNoEditText;
    private EditText feedbackEditText;
    private Button sendButton;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback); // Ensure this matches your XML layout file

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("feedback");

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        enrollmentNoEditText = findViewById(R.id.enrollmentNoEditText);
        mobileNoEditText = findViewById(R.id.mobileNoEditText);
        feedbackEditText = findViewById(R.id.feedbackEditText);
        sendButton = findViewById(R.id.sendButton);

        // Set up button click listener
        sendButton.setOnClickListener(v -> {
            // Perform validation
            if (validateInputs()) {
                // All inputs are valid; handle the feedback submission
                String name = nameEditText.getText().toString().trim();
                String enrollmentNo = enrollmentNoEditText.getText().toString().trim();
                String mobileNo = mobileNoEditText.getText().toString().trim();
                String feedback = feedbackEditText.getText().toString().trim();

                // Create a feedback object
                FeedbackModel feedbackModel = new FeedbackModel(name, enrollmentNo, mobileNo, feedback);

                // Push feedback to Firebase
                databaseReference.push().setValue(feedbackModel)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Failed to send feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private boolean validateInputs() {
        String name = nameEditText.getText().toString().trim();
        String enrollmentNo = enrollmentNoEditText.getText().toString().trim();
        String mobileNo = mobileNoEditText.getText().toString().trim();
        String feedback = feedbackEditText.getText().toString().trim();

        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }
        if (enrollmentNo.isEmpty()) {
            enrollmentNoEditText.setError("Enrollment number is required");
            return false;
        }
        try {
            Integer.parseInt(enrollmentNo);
        } catch (NumberFormatException e) {
            enrollmentNoEditText.setError("Enrollment number must be a number");
            return false;
        }
        if (mobileNo.isEmpty() || mobileNo.length() != 10) {
            mobileNoEditText.setError("Valid mobile number is required");
            return false;
        }
        if (feedback.isEmpty()) {
            feedbackEditText.setError("Feedback is required");
            return false;
        }
        return true;
    }

    // Feedback model class
    public static class FeedbackModel {
        public String name;
        public String enrollmentNo;
        public String mobileNo;
        public String feedback;

        public FeedbackModel() {
            // Default constructor required for calls to DataSnapshot.getValue(FeedbackModel.class)
        }

        public FeedbackModel(String name, String enrollmentNo, String mobileNo, String feedback) {
            this.name = name;
            this.enrollmentNo = enrollmentNo;
            this.mobileNo = mobileNo;
            this.feedback = feedback;
        }
    }
}

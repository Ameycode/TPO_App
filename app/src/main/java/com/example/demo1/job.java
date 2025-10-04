package com.example.demo1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class job extends AppCompatActivity {

    private LinearLayout imageLayout;
    private Button buttonGallery;
    private EditText e1, e2, e3, e4, e5, e6, e7;
    private Button b1;
    private ImageView selectedImageView;
    private Uri imageUri;

    // Firebase Database reference
    private DatabaseReference databaseReference;
    // Firebase Storage reference
    private StorageReference storageReference;

    // ActivityResultLauncher for selecting an image
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Companies");
        storageReference = FirebaseStorage.getInstance().getReference("CompanyLogos");

        // Initialize UI elements
        imageLayout = findViewById(R.id.image_layout);
        buttonGallery = findViewById(R.id.button_gallery);
        selectedImageView =this.<ImageView>findViewById(R.id.selected_image);
        e1 = findViewById(R.id.roll);
        e2 = findViewById(R.id.name);
        e3 = findViewById(R.id.name1);
        e4 = findViewById(R.id.name2);
        e5 = findViewById(R.id.name3);
        e6 = findViewById(R.id.name4);
        e7 = findViewById(R.id.name5);
        b1 = findViewById(R.id.btn1);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // Initialize the ActivityResultLauncher for gallery
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // Get selected image URI
                imageUri = result.getData().getData();
                if (imageUri != null) {
                    displayImage(imageUri);
                }
            }
        });

        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open gallery to select an image
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                galleryLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });
    }

    // Display selected image
    private void displayImage(Uri imageUri) {
        selectedImageView.setImageURI(imageUri);
    }

    // Save data to Firebase
    private void saveData() {
        String companyName = e1.getText().toString().trim();
        String companyCriteria = e2.getText().toString().trim();
        String description = e3.getText().toString().trim();
        String higherPackage = e4.getText().toString().trim();
        String averagePackage = e6.getText().toString().trim();
        String lowerPackage = e5.getText().toString().trim();
        String department = e7.getText().toString().trim();

        if (validateInputs(companyName, companyCriteria, description, higherPackage, averagePackage, lowerPackage, department)) {
            if (imageUri != null) {
                StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveCompanyData(companyName, companyCriteria, description, higherPackage, averagePackage, lowerPackage, department, imageUrl);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(job.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(job.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                });
            } else {
                saveCompanyData(companyName, companyCriteria, description, higherPackage, averagePackage, lowerPackage, department, null);
            }
        }
    }

    private void saveCompanyData(String companyName, String companyCriteria, String description, String higherPackage, String averagePackage, String lowerPackage, String department, String imageUrl) {
        String id = databaseReference.push().getKey();
        Company company = new Company(companyName, companyCriteria, description, higherPackage, averagePackage, lowerPackage, department, imageUrl);
        assert id != null;
        databaseReference.child(id).setValue(company).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(job.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(job.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String companyName, String companyCriteria, String description, String higherPackage, String averagePackage, String lowerPackage, String department) {
        if (TextUtils.isEmpty(companyName)) {
            e1.setError("Company Name is required");
            return false;
        }
        if (TextUtils.isEmpty(companyCriteria)) {
            e2.setError("Company Criteria is required");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            e3.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(higherPackage)) {
            e4.setError("Higher Package is required");
            return false;
        }
        if (TextUtils.isEmpty(averagePackage)) {
            e6.setError("Average Package is required");
            return false;
        }
        if (TextUtils.isEmpty(lowerPackage)) {
            e5.setError("Lower Package is required");
            return false;
        }
        if (TextUtils.isEmpty(department)) {
            e7.setError("Department is required");
            return false;
        }
        return true;
    }

    private void clearFields() {
        e1.setText("");
        e2.setText("");
        e3.setText("");
        e4.setText("");
        e5.setText("");
        e6.setText("");
        e7.setText("");
        selectedImageView.setImageResource(android.R.drawable.ic_menu_add);
    }

    private String getFileExtension(Uri uri) {
        return getContentResolver().getType(uri).split("/")[1];
    }

    // Company data model class
    public static class Company {
        public String companyName, companyCriteria, description, higherPackage, averagePackage, lowerPackage, department, imageUrl;

        public Company() {
            // Default constructor required for calls to DataSnapshot.getValue(Company.class)
        }

        public Company(String companyName, String companyCriteria, String description, String higherPackage, String averagePackage, String lowerPackage, String department, String imageUrl) {
            this.companyName = companyName;
            this.companyCriteria = companyCriteria;
            this.description = description;
            this.higherPackage = higherPackage;
            this.averagePackage = averagePackage;
            this.lowerPackage = lowerPackage;
            this.department = department;
            this.imageUrl = imageUrl;
        }
    }
}

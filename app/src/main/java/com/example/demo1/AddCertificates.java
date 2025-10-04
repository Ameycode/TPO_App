package com.example.demo1;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;

public class AddCertificates extends AppCompatActivity {

    private static final int REQUEST_CODE_PDF = 1;
    private static final int REQUEST_CODE_IMAGE = 2;

    private ArrayList<Uri> pdfUris = new ArrayList<>();
    private Uri imageUri;
    private String enroll;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btn_add_document);
Intent i = getIntent();
enroll = i.getStringExtra("username");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("student");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Button btnAddDocument = findViewById(R.id.btn_add_documents);
        btnAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDocumentDialog();
            }
        });
    }

    private void showAddDocumentDialog() {
        Dialog dialog = new Dialog(AddCertificates.this);
        dialog.setContentView(R.layout.activity_add_certificates);

        EditText editName = dialog.findViewById(R.id.edit_name);
        Button btnUploadPdf = dialog.findViewById(R.id.btn_upload_pdf);
        Button btnUploadImage = dialog.findViewById(R.id.btn_upload_image);
        Button btnDone = dialog.findViewById(R.id.btn_done);

        btnUploadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select PDF(s)"), REQUEST_CODE_PDF);
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE_IMAGE);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(AddCertificates.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(AddCertificates.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                } else if (pdfUris.isEmpty()) {
                    Toast.makeText(AddCertificates.this, "Please upload at least one PDF", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    uploadDataToFirebase(name);
                }
            }
        });

        dialog.show();
    }

    private void uploadDataToFirebase(String name) {
        String userId = databaseReference.push().getKey();
        if (userId == null) {
            Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload image
        StorageReference imageRef = storageReference.child("images/" + userId + ".jpg");
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            // Upload PDFs
            uploadPdfs(userId, name, imageUrl);
        })).addOnFailureListener(e -> Toast.makeText(this, "Image Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadPdfs(String userId, String name, String imageUrl) {
        for (Uri pdfUri : pdfUris) {
            StorageReference pdfRef = storageReference.child("pdfs/" + userId + "/" + pdfUri.getLastPathSegment());
            pdfRef.putFile(pdfUri).addOnSuccessListener(taskSnapshot -> {
                pdfRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String pdfUrl = uri.toString();
                    // Save document data to Realtime Database
                    saveDocumentData(userId, name, imageUrl, pdfUrl);
                });
            }).addOnFailureListener(e -> Toast.makeText(this, "PDF Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void saveDocumentData(String userId, String name, String imageUrl, String pdfUrl) {
        //Document document = new Document(name, imageUrl, pdfUrl);
        databaseReference.child(enroll).child("document").child(name).setValue(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddCertificates.this, "Documents Uploaded Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddCertificates.this, "Failed to upload documents", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_PDF) {
                if (data.getClipData() != null) {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        Uri pdfUri = data.getClipData().getItemAt(i).getUri();
                        pdfUris.add(pdfUri);
                    }
                } else if (data.getData() != null) {
                    Uri pdfUri = data.getData();
                    pdfUris.add(pdfUri);
                }
                Toast.makeText(this, "PDF(s) Uploaded: " + pdfUris.size(), Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_CODE_IMAGE) {
                imageUri = data.getData();
                Toast.makeText(this, "Image Uploaded: " + imageUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class Document {
        public String name;
        public String imageUrl;
        public String pdfUrl;

        public Document() {
        }

        public Document(String name, String imageUrl, String pdfUrl) {
            this.name = name;
            this.imageUrl = imageUrl;
            this.pdfUrl = pdfUrl;
        }
    }
}

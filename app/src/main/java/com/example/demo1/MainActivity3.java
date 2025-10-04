package com.example.demo1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity3 extends AppCompatActivity {

    private EditText enroll, username, email, phone, dob, adhar, percentage;
    private StorageReference storageReference;
    private LinearProgressIndicator progress;

    private RadioGroup gender;
    private Spinner drop;
    private Uri imageUri;
    private TextView uploadImage;
    private ImageView imageView;

    private ListView list;
    String enrollnoid,pass;
    private DatabaseReference databaseReference; // Firebase Database reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Fetch username from intent
        Intent intent = getIntent();
        if (intent != null) {
            enrollnoid = intent.getStringExtra("username");
            pass = intent.getStringExtra( "pass");

        }

        // Rest of your onCreate method continues...
        // Initialize Firebase components
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("students"); // Reference to 'students' node
        progress = findViewById(R.id.progress);
        uploadImage = findViewById(R.id.photo);
        enroll = findViewById(R.id.roll2);
        username = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phoneno);
        dob = findViewById(R.id.editTextDate);
        adhar = findViewById(R.id.editTextNumber);
        percentage = findViewById(R.id.editTextNumberDecimal);
        gender = findViewById(R.id.rg1);
        drop = findViewById(R.id.dropdown);
        imageView = findViewById(R.id.imageView);
        list = findViewById(R.id.list_item);
        Button btn = findViewById(R.id.button);
        TextView s = findViewById(R.id.addcertificates);
        enroll.setText(enrollnoid);
        enroll.setEnabled(false);
        String[] branchS = {"select Branch", "CO", "IT", "EJ", "CE", "ME", "EE"};

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent z = new Intent(MainActivity3.this, AddCertificates.class);
                z.putExtra("username", enrollnoid);
                startActivity(z);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, branchS);
        drop.setAdapter(adapter);

        CardView cd = findViewById(R.id.card);

        dob.setOnClickListener(v -> showDatePickerDialog());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enrollno = enrollnoid;
                String name = username.getText().toString();
                String email1 = email.getText().toString();
                String phone1 = phone.getText().toString();
                String dob1 = dob.getText().toString();
                String adhar1 = adhar.getText().toString();
                String percentage1 = percentage.getText().toString();
                String genderSelection = getSelectedGender();
                enroll.setText(enrollnoid);
                enroll.setEnabled(false);
                // Validation checks
                if (enrollno.isEmpty()) {
                    enroll.setError("Enter Enrollment Number");
                    enroll.requestFocus();
                    return;
                }
                if (enrollno.length() != 10) {
                    enroll.setError("Enter valid Enrollment number");
                    enroll.requestFocus();
                    return;
                }
                if (name.isEmpty()) {
                    username.setError("Enter Name");
                    username.requestFocus();
                    return;
                }
                if (email1.isEmpty()) {
                    email.setError("Enter Email");
                    email.requestFocus();
                    return;
                }
                if (!isValidateEmail(email1)) {
                    email.setError("Enter valid Email");
                    email.requestFocus();
                    return;
                }
                if (phone1.isEmpty() || phone1.length() != 10) {
                    phone.setError("Enter valid Phone number");
                    phone.requestFocus();
                    return;
                }
                if (dob1.isEmpty()) {
                    dob.setError("Enter Date of Birth");
                    dob.requestFocus();
                    return;
                }
                if (adhar1.isEmpty() || adhar1.length() != 12) {
                    adhar.setError("Enter valid Aadhar number");
                    adhar.requestFocus();
                    return;
                }
                if (percentage1.isEmpty()) {
                    percentage.setError("Enter Percentage");
                    percentage.requestFocus();
                    return;
                }
                if (imageUri == null) {
                    Toast.makeText(MainActivity3.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Upload image and then save data to Firebase Database
                uploadImage2(imageUri, enrollnoid, name, email1, phone1, dob1, adhar1, percentage1, genderSelection, drop.getSelectedItem().toString(),pass);
            }
        });
    }

// Rest of your methods...


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity3.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth = selectedMonth + 1;
                    String date = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                    dob.setText(date);
                }, year, month, day);

        datePickerDialog.show();
    }

    public void image(View v) {
        TextView b = findViewById(v.getId());
        if (b.getText().equals("Select Image")) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);
        } else {
            if (imageUri != null) {
                uploadImage2(imageUri, enroll.getText().toString(), username.getText().toString(),
                        email.getText().toString(), phone.getText().toString(),
                        dob.getText().toString(), adhar.getText().toString(),
                        percentage.getText().toString(), getSelectedGender(),drop.getSelectedItem().toString(),pass);
            } else {
                Toast.makeText(MainActivity3.this, "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        uploadImage.setText("Upload Image");
                        Glide.with(MainActivity3.this).load(imageUri).into(imageView);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void uploadImage2(Uri imageUri, String enrollno, String name, String email1, String phone1,
                              String dob1, String adhar1, String percentage1, String genderSelection,String branches,String pass) {
        StorageReference reference = storageReference.child("images/" + System.currentTimeMillis());

        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Save data to Firebase Database under 'students' node
                                Student student = new Student(enrollno, name, email1, phone1, dob1, adhar1, percentage1, genderSelection, uri.toString(), branches,pass);
                                databaseReference.child(enrollno).setValue(student)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(MainActivity3.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                clearForm();
                                                Intent i = new Intent(MainActivity3.this, login.class);
                                                startActivity(i);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity3.this, "Failed to register: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity3.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot snapshot) {
                        long totalBytes = snapshot.getTotalByteCount();
                        long bytesTransferred = snapshot.getBytesTransferred();
                        int progressValue = (int) ((100.0 * bytesTransferred) / totalBytes);
                        progress.setProgress(progressValue);
                    }
                });
    }

    private boolean isValidateEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9,-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private String getSelectedGender() {
        int selectedId = gender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return "";
    }

    private void clearForm() {
        // Clear EditText fields
        enroll.setText("");
        username.setText("");
        email.setText("");
        phone.setText("");
        dob.setText("");
        adhar.setText("");
        percentage.setText("");

        // Clear RadioGroup selection
        gender.clearCheck();

        // Reset Spinner to default selection
        drop.setSelection(0);

        // Reset image related views
        uploadImage.setText("Select Image");
        imageView.setImageResource(android.R.color.transparent);

        // Reset progress indicator
        progress.setProgress(0);
    }
}
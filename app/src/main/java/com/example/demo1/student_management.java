package com.example.demo1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class student_management extends AppCompatActivity {
    private Button btnAddStudent;
   // private Button btnAddExcledata;
    private Button btnChangeStudent;
    private Button btnBlockStudent;
    private Button btnUnblockStudent;
    private ListView listStudents;

    private ArrayList<String> students;
    private ArrayAdapter<String> adapter;
    private int selectedStudentIndex = -1;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        btnAddStudent = findViewById(R.id.btn_add_student);
        //btnAddExcledata = findViewById(R.id.btn_add_excel_sheet);
        btnChangeStudent = findViewById(R.id.btn_change_student);
        btnBlockStudent = findViewById(R.id.btn_block_student);
        btnUnblockStudent = findViewById(R.id.btn_unblock_student);
        listStudents = findViewById(R.id.list_students);

        students = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        listStudents.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        loadStudentsFromFirebase();

        listStudents.setOnItemClickListener((parent, view, position, id) -> selectedStudentIndex = position);

        btnAddStudent.setOnClickListener(v -> {
            Intent i = new Intent(student_management.this, Admin_page.class);
            startActivity(i);
        });

        //btnAddExcledata.setOnClickListener(v -> {
            //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            //intent.addCategory(Intent.CATEGORY_OPENABLE);
            //intent.setType("*/*"); // Allow all file types for selection
            //startActivityForResult(intent, 1);
        //});

        btnChangeStudent.setOnClickListener(v -> {
            if (selectedStudentIndex != -1) {
                showInputDialog("Change Student Details", "Enter new student name:", input -> {
                    changeStudentInFirebase(selectedStudentIndex, input);
                });
            } else {
                Toast.makeText(student_management.this, "No Student Selected to Change", Toast.LENGTH_SHORT).show();
            }
        });

        btnBlockStudent.setOnClickListener(v -> {
            if (selectedStudentIndex != -1) {
                blockStudentInFirebase(selectedStudentIndex);
            } else {
                Toast.makeText(student_management.this, "No Student Selected to Block", Toast.LENGTH_SHORT).show();
            }
        });

        btnUnblockStudent.setOnClickListener(v -> {
            if (selectedStudentIndex != -1 && students.get(selectedStudentIndex).contains("(Blocked)")) {
                unblockStudentInFirebase(selectedStudentIndex);
            } else {
                Toast.makeText(student_management.this, "No Student Selected to Unblock", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Log.d("FileSelected", "Uri: " + uri.toString());
            if (uri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    readExcelFile(inputStream);
                } catch (FileNotFoundException e) {
                    Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private void readExcelFile(InputStream inputStream) {
        try {
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 1; i < sheet.getRows(); i++) { // Skip the header row
                String enrollno = sheet.getCell(0, i).getContents().trim();
                String name = sheet.getCell(1, i).getContents().trim();
                String pass = sheet.getCell(2, i).getContents().trim();
                if (!enrollno.isEmpty() && !name.isEmpty()) {
                    addStudentToFirebase(enrollno, name, pass);
                }
            }
            Toast.makeText(this, "Excel data added successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "IO Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (BiffException e) {
            Toast.makeText(this, "Biff Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(this, "Error reading Excel file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addStudentToFirebase(String enrollno, String name, String pass) {
        String studentId = databaseReference.push().getKey(); // Generate a unique key for each student
        Student student = new Student(studentId, enrollno, name, pass, "Active"); // Create a new Student object
        databaseReference.child(studentId).setValue(student) // Save the student to Firebase
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Student added successfully: " + name);
                    students.add(enrollno + " - " + name +" - "+ pass); // Update the local list
                    adapter.notifyDataSetChanged(); // Refresh the ListView
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add student: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadStudentsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                students.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String enrollno = snapshot.child("enrollno").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String student = enrollno + " - " + name;
                    if (status != null && status.equals("Blocked")) {
                        student += " (Blocked)";
                    }
                    students.add(student);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(student_management.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeStudentInFirebase(int index, String newName) {
        String studentId = students.get(index).split(" - ")[0];
        databaseReference.child(studentId).child("name").setValue(newName);
    }

    private void blockStudentInFirebase(int index) {
        String studentId = students.get(index).split(" - ")[0];
        databaseReference.child(studentId).child("status").setValue("Blocked");
    }

    private void unblockStudentInFirebase(int index) {
        String studentId = students.get(index).split(" - ")[0];
        databaseReference.child(studentId).child("status").setValue("Active");
    }

    private void showInputDialog(String title, String message, final InputDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String userInput = input.getText().toString().trim();
            if (!userInput.isEmpty()) {
                listener.onInput(userInput);
            } else {
                Toast.makeText(getApplicationContext(), "Input cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private interface InputDialogListener {
        void onInput(String input);
    }

    public class Student {
        public String studentId;
        public String enrollno;
        public String name;
        public String status;

        public Student() {
        }

        public Student(String studentId, String enrollno, String name, String status, String active) {
            this.studentId = studentId;
            this.enrollno = enrollno;
            this.name = name;
            this.status = status;
        }
    }
}

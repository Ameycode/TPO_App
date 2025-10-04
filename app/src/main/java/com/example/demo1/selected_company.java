package com.example.demo1;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class selected_company extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private CardView cardView;
    private ListView listView;

    private DatabaseReference applicationStatusReference;
    private ArrayList<String> applicationStatusList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_company);

        // Access the components
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        cardView = findViewById(R.id.cardView);
        listView = findViewById(R.id.listView);

        // Initialize Firebase reference
        applicationStatusReference = FirebaseDatabase.getInstance().getReference("ApplicationStatus");

        // Initialize ArrayList and ArrayAdapter
        applicationStatusList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, applicationStatusList);
        listView.setAdapter(adapter);

        // Load application status data from Firebase
        loadApplicationStatusFromFirebase();
    }

    private void loadApplicationStatusFromFirebase() {
        applicationStatusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                applicationStatusList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String enroll = snapshot.child("enroll").getValue(String.class);
                    String opportunity = snapshot.child("opportunity").getValue(String.class);
                    if (enroll != null && opportunity != null) {
                        String status = "\nEnrollment: " + enroll + "\nOpportunity: " + opportunity;
                        applicationStatusList.add(status);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(selected_company.this, "Failed to load application status", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

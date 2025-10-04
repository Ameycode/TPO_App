package com.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class company_opportunity extends AppCompatActivity {

    private static final String TAG = "company_opportunity";

    private Button btnAddOpportunity;
    private ListView listOpportunities;
    private ArrayList<Company> opportunities;
    private CompanyAdapter adapter;

    // Firebase Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_opportunity);

        // Initialize the UI components
        btnAddOpportunity = findViewById(R.id.btn_add_opportunity);
        listOpportunities = findViewById(R.id.list_opportunities);

        // Initialize the list and adapter
        opportunities = new ArrayList<>();
        adapter = new CompanyAdapter(this, opportunities);
        listOpportunities.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Companies");

        // Set up the button click listener
        btnAddOpportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOpportunity();
            }
        });

        // Fetch data from Firebase
        fetchOpportunitiesFromFirebase();
    }

    private void fetchOpportunitiesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                opportunities.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);
                    opportunities.add(company);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(company_opportunity.this, "Failed to load opportunities.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addOpportunity() {

        startActivity(new Intent(this, job.class));
    }
}

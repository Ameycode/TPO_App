package com.example.demo1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class view_opportunity extends AppCompatActivity {

    private ListView lvOpportunities;
    private Button btnApplyOpportunity;
    private ArrayList<String> opportunities;
    private ArrayList<Company> companyList;
    private String selectedOpportunity;

    private DatabaseReference databaseReference;
    private DatabaseReference applicationStatusReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_opportunity);

        lvOpportunities = findViewById(R.id.lvOpportunities);
        btnApplyOpportunity = findViewById(R.id.btnApplyOpportunity);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("Companies");
        applicationStatusReference = FirebaseDatabase.getInstance().getReference("ApplicationStatus");

        opportunities = new ArrayList<>();
        companyList = new ArrayList<>();

        // Set up ListView with sample data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, opportunities);
        lvOpportunities.setAdapter(adapter);
        lvOpportunities.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // Load company data from Firebase
        loadCompaniesFromFirebase(adapter);

        // Set up ListView item click listener
        lvOpportunities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOpportunity = opportunities.get(position);
            }
        });

        // Set up Apply button click listener
        btnApplyOpportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOpportunity != null) {
                    promptForEnrollNumber();
                } else {
                    Toast.makeText(view_opportunity.this, "Please select an opportunity to apply for", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCompaniesFromFirebase(ArrayAdapter<String> adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                opportunities.clear();
                companyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Company company = snapshot.getValue(Company.class);
                    if (company != null) {
                        String opportunity = company.getCompanyName() + " - " + company.getDescription();
                        opportunities.add(opportunity);
                        companyList.add(company);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view_opportunity.this, "Failed to load opportunities", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void promptForEnrollNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Enrollment Number");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enroll = input.getText().toString().trim();
                if (!enroll.isEmpty()) {
                    applyForOpportunity(enroll, selectedOpportunity);
                } else {
                    Toast.makeText(view_opportunity.this, "Enrollment number cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void applyForOpportunity(String enroll, String opportunity) {
        String opportunityKey = applicationStatusReference.push().getKey();
        Map<String, String> applicationData = new HashMap<>();
        applicationData.put("enroll", enroll);
        applicationData.put("opportunity", opportunity);

        if (opportunityKey != null) {
            applicationStatusReference.child(opportunityKey).setValue(applicationData)
                    .addOnSuccessListener(aVoid -> Toast.makeText(view_opportunity.this, "Applied for: " + opportunity, Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(view_opportunity.this, "Failed to apply for: " + opportunity, Toast.LENGTH_SHORT).show());
        }
    }
}

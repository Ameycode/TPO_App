package com.example.demo1;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class application_status extends AppCompatActivity {

    private ListView lvApplicationStatus;
   // private Button btnCheckStatus;

    private ArrayList<String> notifications;
    private ArrayAdapter<String> adapter;

    private DatabaseReference notificationsReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_status);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);

        TextView tvStatusTitle = findViewById(R.id.tvStatusTitle);
        lvApplicationStatus = findViewById(R.id.lvApplicationStatus);


        notifications = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notifications);
        lvApplicationStatus.setAdapter(adapter);

        // Initialize Firebase
        notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");

        fetchNotificationsFromFirebase();
    }

    private void fetchNotificationsFromFirebase() {
        notificationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifications.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String message = snapshot.getValue(String.class);
                    if (message != null) {
                        notifications.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
                lvApplicationStatus.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(application_status.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

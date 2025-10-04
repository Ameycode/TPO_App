package com.example.demo1;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.demo1.profile;
import com.example.demo1.view_opportunity;

public class StudentDashboard extends AppCompatActivity {
    CardView profile,status,opportunity,feedback;
    String username,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        profile = findViewById(R.id.profileCard);
        status = findViewById(R.id.statuscard);
        opportunity= findViewById(R.id.OpportunitiesCard);
        feedback= findViewById(R.id.feedbackCardView);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentDashboard.this,MainActivity3.class);
                Intent intent = getIntent();

                if (intent != null) {
                    username = intent.getStringExtra("username");
                    pass = intent.getStringExtra( "pass");

                    // Now you have username, you can use it as needed
                }
                i.putExtra("username",username);
                i.putExtra("pass",pass);
                startActivity(i);
            }
        });
        opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentDashboard.this, view_opportunity.class);
                i.putExtra("username",username);
                i.putExtra("pass",pass);
                startActivity(i);
            }
        });
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentDashboard.this,application_status.class);
                i.putExtra("username",username);
                i.putExtra("pass",pass);
                startActivity(i);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentDashboard.this,feedback.class);
                i.putExtra("username",username);
                i.putExtra("pass",pass);
                startActivity(i);
            }
        });
    }



}
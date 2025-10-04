package com.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashBoard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dash_board);

        Button btn1 = findViewById(R.id.Company_opportunity);
        Button btn2 = findViewById(R.id.student_management);
        Button btn3 = findViewById(R.id.notification);
        //Button btn4 = findViewById(R.id.selected_company);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open1();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open2();

            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open3();
            }
        });

       /* btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                open4();
            }
        });*/
    }

    public void open1()
    {
        Intent i1=new Intent(AdminDashBoard.this, company_opportunity.class);
        startActivity(i1);
    }

    public void open2()
    {
        Intent i2=new Intent(AdminDashBoard.this,student_management.class);
        startActivity(i2);
    }

    public void open3()
    {
        Intent i3=new Intent(AdminDashBoard.this, notification.class);
        startActivity(i3);
    }

   /* public void open4()
    {
        Intent i4=new Intent(AdminDashBoard.this, feedback_fetch.class);
        startActivity(i4);
    }*/


}
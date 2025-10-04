package com.example.demo1;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.tabs.TabLayout;

public class home_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page); // Ensure the layout file name matches your XML file

        // Initialize TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        // Set the default fragment to HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_area, new HomeFragment())
                .commit();

        // Add a listener for tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;

                // Determine which fragment to display
                switch (tab.getPosition()) {
                    case 0: // Home Tab
                        selectedFragment = new HomeFragment();
                        break;
                    case 1: // Login Tab
                        selectedFragment = new LoginFragment();
                        break;
                }

                // Replace the current fragment with the selected fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_area, selectedFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}

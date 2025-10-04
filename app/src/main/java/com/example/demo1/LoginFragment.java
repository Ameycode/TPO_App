package com.example.demo1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Set up a listener or an event to trigger the activity start
        view.findViewById(R.id.admin_login_button).setOnClickListener(v -> {
            // Start the login activity
            Intent i = new Intent(getActivity(), login.class);
            startActivity(i);
        });

        view.findViewById(R.id.student_login_button).setOnClickListener(v -> {
            // Start the login activity
            Intent i = new Intent(getActivity(), login.class);
            startActivity(i);
        });

        return view;
    }
}

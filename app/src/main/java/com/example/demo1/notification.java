package com.example.demo1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class notification extends AppCompatActivity {

    private EditText etNotificationMessage;
    private Button btnSendNotification;
    private ListView listNotifications;

    private ArrayList<String> notificationMessages;
    private ArrayAdapter<String> adapter;

    private static final String CHANNEL_ID = "notification_channel";
    private static final int NOTIFICATION_ID = 1;

    private DatabaseReference notificationsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        etNotificationMessage = findViewById(R.id.et_notification_message);
        btnSendNotification = findViewById(R.id.btn_send_notification);
        listNotifications = findViewById(R.id.list_notifications);

        notificationMessages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationMessages);
        listNotifications.setAdapter(adapter);

        // Initialize Firebase
        notificationsReference = FirebaseDatabase.getInstance().getReference("Notifications");

        createNotificationChannel();

        // Load notifications from Firebase
        loadNotificationsFromFirebase();

        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etNotificationMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendNotification(message);
                    saveNotificationToFirebase(message);
                    addMessageToList(message);
                } else {
                    Toast.makeText(notification.this, "Please enter a notification message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notification app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.noti)
                .setContentTitle("New Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private void saveNotificationToFirebase(String message) {
        String notificationId = notificationsReference.push().getKey();
        if (notificationId != null) {
            notificationsReference.child(notificationId).setValue(message)
                    .addOnSuccessListener(aVoid -> Toast.makeText(notification.this, "Notification sending successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(notification.this, "Failed to send notification", Toast.LENGTH_SHORT).show());
        }
    }

    private void loadNotificationsFromFirebase() {
        notificationsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationMessages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String message = snapshot.getValue(String.class);
                    if (message != null) {
                        notificationMessages.add(message);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(notification.this, "Failed to load notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessageToList(String message) {
        notificationMessages.add(message);
        adapter.notifyDataSetChanged();
    }
}

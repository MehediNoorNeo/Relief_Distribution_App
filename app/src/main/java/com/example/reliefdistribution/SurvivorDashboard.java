package com.example.reliefdistribution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SurvivorDashboard extends AppCompatActivity {

    LinearLayout linearLayout, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6;

    private ImageView ivHomeIcon, ivChatIcon, ivNotificationIcon, ivProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survivor_dashboard);

        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout4 = findViewById(R.id.linearLayout4);
        linearLayout5 = findViewById(R.id.linearLayout5);
        linearLayout6 = findViewById(R.id.linearLayout6);

        ivHomeIcon = findViewById(R.id.ivHomeIcon);
        ivChatIcon = findViewById(R.id.ivChatIcon);
        ivNotificationIcon = findViewById(R.id.ivNotificationIcon);
        ivProfileIcon = findViewById(R.id.ivProfileIcon);

        linearLayout4.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:999"));
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        });

        linearLayout2.setOnClickListener(view -> {
            Intent intent = new Intent(this, Informations.class);
            startActivity(intent);
        });
        ivProfileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(SurvivorDashboard.this, Profile.class);
            startActivity(intent);
        });

    }
}
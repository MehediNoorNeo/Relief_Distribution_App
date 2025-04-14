package com.example.reliefdistribution;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Informations extends AppCompatActivity {

    ImageView ivHomeIcon, ivChatIcon, ivNotificationIcon, ivProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informations);

        ivHomeIcon = findViewById(R.id.ivHomeIcon);
        ivChatIcon = findViewById(R.id.ivChatIcon);
        ivNotificationIcon = findViewById(R.id.ivNotificationIcon);
        ivProfileIcon = findViewById(R.id.ivProfileIcon);

        ivHomeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Informations.this, SurvivorDashboard.class));
                finish();
            }
        });
        ivProfileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Informations.this, Profile.class);
            startActivity(intent);
        });

    }
}
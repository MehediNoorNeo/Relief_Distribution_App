package com.example.reliefdistribution;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import android.Manifest;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DistributorDashboard extends AppCompatActivity {

    private Switch distributeModeSwitch;
    private LinearLayout ltrackig, lEmergency, lFeatures, lSettings;
    private boolean isDistributeModeOn;

    private ImageView ivHomeIcon, ivChatIcon, ivNotificationIcon, ivProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_dashboard);

        distributeModeSwitch = findViewById(R.id.switch_on_off);
        ltrackig = findViewById(R.id.lTracking);
        lEmergency = findViewById(R.id.lEmergency);
        lFeatures = findViewById(R.id.lFeatures);
        lSettings = findViewById(R.id.lSettings);
        ivHomeIcon = findViewById(R.id.ivHomeIcon);
        ivChatIcon = findViewById(R.id.ivChatIcon);
        ivNotificationIcon = findViewById(R.id.ivNotificationIcon);
        ivProfileIcon = findViewById(R.id.ivProfileIcon);

         distributeModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDistributeModeOn = isChecked;
            if (isChecked) {
                Toast.makeText(this, "Distribute Mode ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Distribute Mode OFF", Toast.LENGTH_SHORT).show();
            }
        });

        ltrackig.setOnClickListener(view -> {
            Intent intent = new Intent(DistributorDashboard.this, GoogleMap.class);
            intent.putExtra("distributeMode", isDistributeModeOn);
            startActivity(intent);
        });
        lEmergency.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:999"));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        });
        ivProfileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(DistributorDashboard.this, Profile.class);
            startActivity(intent);
        });
        ivHomeIcon.setOnClickListener(view -> {
            Intent intent = new Intent(DistributorDashboard.this, DistributorDashboard.class);
        });

    }
}

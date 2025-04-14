package com.example.reliefdistribution;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class GoogleMap extends AppCompatActivity implements OnMapReadyCallback {

    private com.google.android.gms.maps.GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Marker userMarker;
    private DatabaseReference databaseReference;
    private boolean isDistributeModeOn;
    private String fName, userId, email;

    ImageView ivHomeIcon, ivProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        ivHomeIcon = findViewById(R.id.ivHomeIcon);
        ivProfileIcon = findViewById(R.id.ivProfileIcon);

        ivHomeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationUpdates();
                startActivity(new Intent(GoogleMap.this, DistributorDashboard.class));
                finish();
            }
        });
        ivProfileIcon.setOnClickListener(view -> {
            Intent intent = new Intent(GoogleMap.this, Profile.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        isDistributeModeOn = intent.getBooleanExtra("distributeMode", false);

        SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        userId = preferences.getString("registeredUserId", null);
        email = preferences.getString("registeredEmail", null);

        if (userId == null) {
            Toast.makeText(this, "User data not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.e("GoogleMap", "Map fragment is null!");
        }

        toggleDistributeMode(isDistributeModeOn);
    }

    @Override
    public void onMapReady(@NonNull com.google.android.gms.maps.GoogleMap googleMap) {
        myMap = googleMap;

        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(true);

        LatLng bangladesh = new LatLng(23.6850, 90.3563);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangladesh, 10));
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null || !isDistributeModeOn) {
                    stopLocationUpdates();
                    return;
                }

                LatLng userLocation = new LatLng(
                        locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude()
                );

                if (userMarker != null) {
                    userMarker.setPosition(userLocation);
                    userMarker.setTitle(email);
                } else {
                    userMarker = myMap.addMarker(new MarkerOptions().position(userLocation).title(email));
                }

                 float currentZoom = myMap.getCameraPosition().zoom;
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, currentZoom));

                saveLocationToDatabase(userLocation);
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }


    private void stopLocationUpdates() {
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

        if (userMarker != null) {
            userMarker.remove();
            userMarker = null;
        }

        if (databaseReference != null && userId != null) {
            databaseReference.child(userId).child("location").removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Location removed from database.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to remove location from database.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void toggleDistributeMode(boolean distributeMode) {
        isDistributeModeOn = distributeMode;
        if (isDistributeModeOn) {
            startLocationUpdates();
        } else {
            stopLocationUpdates();
        }
    }

    private void saveLocationToDatabase(LatLng location) {
        if (isDistributeModeOn && databaseReference != null && userId != null) {
            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("latitude", location.latitude);
            locationMap.put("longitude", location.longitude);

            databaseReference.child(userId).child("location").setValue(locationMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Location updated in database.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Failed to update location.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(this, "Location permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }
}

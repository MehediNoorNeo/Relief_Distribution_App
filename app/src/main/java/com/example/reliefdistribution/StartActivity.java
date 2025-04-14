package com.example.reliefdistribution;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartActivity extends AppCompatActivity {

    Button btStart;

    FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
             String userId = currentUser.getUid();
            database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            Intent intent;
                            if ("Distributor".equals(user.getRole())) {
                                intent = new Intent(StartActivity.this, DistributorDashboard.class);
                            } else {
                                intent = new Intent(StartActivity.this, SurvivorDashboard.class);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            mAuth.signOut();
                            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        mAuth.signOut();
                        Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btStart = findViewById(R.id.btStart);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");

        btStart.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

}

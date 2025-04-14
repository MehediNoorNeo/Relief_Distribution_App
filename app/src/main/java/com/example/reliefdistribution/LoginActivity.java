package com.example.reliefdistribution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Spinner spinner;

    TextView tvSignUp;
    EditText etEmail, etPassword;
    Button btLogin;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = findViewById(R.id.dropdown_menu);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(LoginActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedRole = spinner.getSelectedItem().toString();
            loginUser(email, password, selectedRole);
        });

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password, String selectedRole) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            checkUserRole(currentUser.getUid(), selectedRole);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String userId, String selectedRole) {
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && user.getRole().equals(selectedRole)) {
                        saveLoginData(userId);
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent;
                        if (selectedRole.equals("Distributor")) {
                            intent = new Intent(LoginActivity.this, DistributorDashboard.class);
                        } else {
                            intent = new Intent(LoginActivity.this, SurvivorDashboard.class);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid role selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            private void saveLoginData(String userId) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();

                SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRegistered", true);
                editor.putString("registeredEmail", email);
                editor.putString("registeredPassword", password);
                editor.putString("registeredUserId", userId);
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

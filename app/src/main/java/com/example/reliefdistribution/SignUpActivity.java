package com.example.reliefdistribution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private Spinner spinner;
    EditText etFName, etLName, etEmail, etPhoneNumber, etPassword, etConfirmPassword;
    Button btSignUp;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        spinner = findViewById(R.id.dropdown_menu);
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btSignUp = findViewById(R.id.btSignUp);

        String[] items = {"Distributor", "Survivor"};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_items,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(SignUpActivity.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btSignUp.setOnClickListener(v -> {
            String fName = etFName.getText().toString().trim();
            String lName = etLName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhoneNumber.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String selectedRole = spinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(lName) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

             registerUser(fName, lName, email, phone, selectedRole, password);
        });
    }

    private void registerUser(String fName, String lName, String email, String phone, String role, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                         saveUserToDatabase(fName, lName, email, phone, role, password);
                    } else {
                         Toast.makeText(SignUpActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDatabase(String fName, String lName, String email, String phone, String role, String password) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");
        String userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();

        User user = new User(userId, fName, lName, email, phone, role, password);
        database.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                     SharedPreferences preferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isRegistered", true);
                    editor.putString("registeredFName", fName);
                    editor.putString("registeredLName", lName);
                    editor.putString("registeredEmail", email);
                    editor.putString("registeredPhone", phone);
                    editor.putString("registeredRole", role);
                    editor.putString("registeredPassword", password);
                    editor.putString("registeredUserId", userId);
                    editor.apply();

                    Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show();

                     if (role.equals("Distributor")) {
                        Intent intent = new Intent(SignUpActivity.this, DistributorDashboard.class);
                        startActivity(intent);
                        finish();
                    } else if (role.equals("Survivor")) {
                        Intent intent = new Intent(SignUpActivity.this, SurvivorDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to Save User: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

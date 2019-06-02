package com.example.foodorderingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput;
    private EditText phoneNumberInput;
    private EditText emailAddressInput;
    private EditText passwordInput;
    private Button signUpButton;
    private ProgressBar mProgressBar;

    // Member Variables
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    // Constants
    public static final String APP_PREFS = "userDetails";
    public static final String DISPLAY_NAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nameInput = findViewById(R.id.nameInput);
        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        emailAddressInput = findViewById(R.id.emailAddressInput);
        passwordInput = findViewById(R.id.passwordInput);
        signUpButton = findViewById(R.id.buttonSignUp);
        mProgressBar = findViewById(R.id.progressBar);

        mReference = FirebaseDatabase.getInstance().getReference().child("user");
        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp_Click(View view) {

        // Display Progress bar on screen
        mProgressBar.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.GONE);

        String name = checkName(String.valueOf(nameInput.getText()).trim());
        String phoneNumber = checkPhone(String.valueOf(phoneNumberInput.getText()).trim());
        String email = checkEmail(String.valueOf(emailAddressInput.getText()).trim());
        String password = checkPassword(String.valueOf(passwordInput.getText()).trim());
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            // Call CreateAccount method
            CreateAccount(name, phoneNumber, email, password);
        } else {

            // Call ShowError method
            ShowError(name, phoneNumber, email, password);
            mProgressBar.setVisibility(View.GONE);
            signUpButton.setVisibility(View.VISIBLE);
        }
    }

    private void ShowError(String name, String phoneNumber, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Please enter valid name!");
            nameInput.requestFocus();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberInput.setError("Please provide valid mobile number!");
            phoneNumberInput.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            emailAddressInput.setError("Please provide valid email!");
        } else if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password must be 6 characters long!");
        }
    }

    private void CreateAccount(final String name, final String phoneNumber, final String email, final String password) {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(phoneNumber).exists()) {
                    saveDisplayName(name);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference currentUserRef = mReference.child(phoneNumber);
                                currentUserRef.child("name").setValue(name);
                                currentUserRef.child("email").setValue(email);

                                Toast.makeText(SignUpActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                // Go to login activity
                                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(loginIntent);
                            } else {
                                emailAddressInput.setError("Email Already Registered!");
                                emailAddressInput.requestFocus();
                                mProgressBar.setVisibility(View.GONE);
                                signUpButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else if(dataSnapshot.child(phoneNumber).exists()) {
                    phoneNumberInput.setError("Phone Number Already Registered!");
                    phoneNumberInput.requestFocus();
                    mProgressBar.setVisibility(View.GONE);
                    signUpButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Save the display name to Shared Preferences
    private void saveDisplayName(String name) {
        SharedPreferences prefs = getSharedPreferences(APP_PREFS, 0);
        prefs.edit().putString(DISPLAY_NAME_KEY, name).apply();
    }

    // Password validation
    private String checkPassword(String password) {
        if (password.length() < 6) {
            return null;
        } else {
            return password;
        }
    }

    // Email Validation
    private String checkEmail(String email) {
        if (!email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
            return null;
        } else {
            return email;
        }
    }

    // Mobile number validation
    private String checkPhone(String phone) {
        if (!phone.matches("[0-9]{11}")) {
            return null;
        } else {
            return phone;
        }
    }

    // Name validation
    private String checkName(String name) {
        if (!name.matches("[a-zA-Z]{3,}")) {
            return null;
        } else {
            return name;
        }
    }
}

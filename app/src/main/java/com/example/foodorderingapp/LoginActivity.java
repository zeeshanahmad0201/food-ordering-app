package com.example.foodorderingapp;

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

import com.example.foodorderingapp.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity {

    private EditText userEmail;
    private EditText userPassword;
    private Button loginButton;
    private ProgressBar mProgressBar;

    private String mDisplayPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = (findViewById(R.id.emailAddressInput));
        userPassword = (findViewById(R.id.passwordInput));
        mProgressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);

                UserDetails user = new UserDetails();

                user.setEmail(userEmail.getText().toString().trim());
                user.setPassword(userPassword.getText().toString().trim());

                String email = user.getEmail();
                String password = user.getPassword();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    login(email, password);
                }
            }
        });
    }

    private void login(String email, String password) {

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("user");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Go to home activity
                    UserDetails user = new UserDetails();
                    user.setPhone(mDisplayPhone);
                    Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(homeIntent);

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
                loginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    // Retrieve the display name from the Shared Preferences
    private void setupDisplayName() {
        SharedPreferences prefs = getSharedPreferences(SignUpActivity.APP_PREFS, MODE_PRIVATE);
        mDisplayPhone = prefs.getString(SignUpActivity.DISPLAY_PHONE_KEY, "");
    }
}

package com.example.code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    TextInputEditText loginEmailET;
    TextInputEditText loginPasswordET;
    TextView signUpTV;
    Button loginButton;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize Everything
        signUpTV = findViewById(R.id.signUpPrompt);
        loginEmailET = findViewById(R.id.emailEditText);
        loginPasswordET = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(view -> {
            loginUser();
        });
        signUpTV.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
    private void loginUser() {
        String email = loginEmailET.getText().toString();
        String password = loginPasswordET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            loginEmailET.setError("Email cannot be empty.");
            loginEmailET.requestFocus();
        } else if (TextUtils.isEmpty(password))  {
            loginPasswordET.setError("Password cannot be empty.");
            loginPasswordET.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User Login Successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, ConnectSpotifyPage.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "User Login Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
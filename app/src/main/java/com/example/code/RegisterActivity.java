package com.example.code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.code.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    Button regBtn;
    FirebaseAuth mAuth;
    TextView loginHereTV;
    TextInputEditText registerEmailET;
    TextInputEditText registerPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regBtn = findViewById(R.id.registerButton);
        loginHereTV = findViewById(R.id.loginPrompt);
        registerEmailET = findViewById(R.id.emailRegisterEditText);
        registerPasswordET= findViewById(R.id.passwordRegisterEditText);

        mAuth = FirebaseAuth.getInstance();
        regBtn.setOnClickListener(view -> {
            createUser();
        });

        loginHereTV.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

    }
    private void createUser() {
        String email = registerEmailET.getText().toString();
        String password = registerPasswordET.getText().toString();

        //if edit text is blank, show error
        if (TextUtils.isEmpty(email)) {
            registerEmailET.setError("Email cannot be empty.");
            registerEmailET.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            registerPasswordET.setError("Password cannot be empty.");
            registerPasswordET.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, "User registered error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
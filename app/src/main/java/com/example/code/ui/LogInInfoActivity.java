package com.example.code.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.code.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInInfoActivity extends AppCompatActivity {
    ImageButton go_back_button;
    TextView myEmail,myPassword;
    FirebaseUser user;
    FirebaseAuth auth;
    String eMail, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_info);
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(view -> {
            startActivity(new Intent(LogInInfoActivity.this, ProfileActivity.class));
        });

        myEmail = findViewById(R.id.myEmail);
        myPassword = findViewById(R.id.current_password);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        try {
            eMail = user.getEmail();

        } catch (NullPointerException e) {
            eMail = "No email";
        }
        myEmail.setText(eMail);
        myPassword.setText("Reset Password");
        myPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(LogInInfoActivity.this, R.style.MyDialogTheme);
                dialog.setTitle("Reset password via your email");
                dialog.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.sendPasswordResetEmail(eMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LogInInfoActivity.this, "Email sent", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LogInInfoActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LogInInfoActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }
}
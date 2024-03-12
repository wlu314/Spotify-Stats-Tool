package com.example.code.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.code.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeletAccountActivity extends AppCompatActivity {
    ImageButton go_back_button;
    Button deleteAccountButton;
    ProgressBar deleteAccountPB;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delet_account);
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(view -> {
            startActivity(new Intent(DeletAccountActivity.this, ProfileActivity.class));
        });


        //delete account
        deleteAccountButton = findViewById(R.id.button_delete_account);
        deleteAccountPB = findViewById(R.id.progressBar_delete_account);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DeletAccountActivity.this, R.style.MyDialogTheme);
                dialog.setTitle("Are you Sure??");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you won't be able to retrive the data");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccountPB.setVisibility(View.VISIBLE);
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                deleteAccountPB.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(DeletAccountActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(DeletAccountActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(DeletAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
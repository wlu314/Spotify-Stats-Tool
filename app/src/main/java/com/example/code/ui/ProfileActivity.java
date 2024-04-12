package com.example.code.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.code.R;

public class ProfileActivity extends AppCompatActivity {
    boolean darkmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton go_back_button;
    TextView accountName,logInInfo,dark_mode,profile_notification,account_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(view -> {
         startActivity(new Intent(ProfileActivity.this, Statistics.class));
        });

        logInInfo = findViewById(R.id.logInInfo);
        logInInfo.setOnClickListener(view -> {
         startActivity(new Intent(ProfileActivity.this, LogInInfoActivity.class));
        });

        profile_notification = findViewById(R.id.profile_notification);
        profile_notification.setOnClickListener(view -> {
            startActivity(new Intent(ProfileActivity.this, Notifications.class));
        });
        account_delete = findViewById(R.id.account_delete);
        account_delete.setOnClickListener(view -> {
         startActivity(new Intent(ProfileActivity.this, DeleteAccountActivity.class));
        });
    }

    public void darkMode(View view) {
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkmode = sharedPreferences.getBoolean("darkMode", false);
        if (darkmode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", false);
            editor.apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor = sharedPreferences.edit();
            editor.putBoolean("darkMode", true);
            editor.apply();
        }

    }
}
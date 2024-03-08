package com.example.code;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {
    ImageButton go_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
     go_back_button = findViewById(R.id.go_back_button);
     go_back_button.setOnClickListener(view -> {
         startActivity(new Intent(ProfileActivity.this, Statistics.class));
     });


    }
}
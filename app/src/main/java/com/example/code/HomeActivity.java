package com.example.code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.getMenu().findItem(R.id.navigation_home).setChecked(true);
        nav_view.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                Activity current = HomeActivity.this;
                if (item.getItemId() == R.id.navigation_home) {
                    intent = new Intent(current, HomeActivity.class);
                } else if (item.getItemId() == R.id.navigation_stats) {
                    intent = new Intent(current, Statistics.class);
                } else if (item.getItemId() == R.id.navigation_friend) {
                    intent = new Intent(current, FriendActivity.class);
                } else if (item.getItemId() == R.id.navigation_game) {
                    intent = new Intent(current, GameActivity.class);
                }
                if (intent != null) {
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }


}
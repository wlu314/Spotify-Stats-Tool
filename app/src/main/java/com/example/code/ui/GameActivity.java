package com.example.code.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.code.R;
import com.example.code.StartGame;
import com.google.android.material.bottomnavigation.BottomNavigationView;



public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        nav_view.getMenu().findItem(R.id.navigation_game).setChecked(true);
        nav_view.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                Activity current = GameActivity.this;
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

    public void startGame(View view) {
        // In startGame() method, create an Intent to launch StartGame Activity
        Intent intent = new Intent(GameActivity.this, StartGame.class);
        startActivity(intent);
        // Finish MainActivity
        finish();
    }


}
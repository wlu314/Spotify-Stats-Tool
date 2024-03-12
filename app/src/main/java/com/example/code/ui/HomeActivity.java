package com.example.code.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.code.R;
import com.example.code.SpotifyAPI.SpotifyOauth2Application;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private SpotifyOauth2Application spotifyOauth2Application;
    SwitchCompat darkmodeSwitch;
    boolean darkmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        darkmodeSwitch = findViewById(R.id.dark_mode);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkmode = sharedPreferences.getBoolean("darkMode", false);

        if (darkmode) {
            darkmodeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        darkmodeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (darkmode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("darkMode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("darkMode", true);
                }
                editor.apply();
            }
        });


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
package com.example.code.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.code.R;
import com.example.code.SpotifyAPI.SpotifyTopItemsActivity;
import com.example.code.SpotifyAPI.SpotifyUserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    SwitchCompat darkmodeSwitch;
    boolean darkmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button logout;
    FirebaseUser user;
    FirebaseAuth auth;
    String[] item = {"2024","2023","2022"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    Button btnViewSpotifyProfile, btntopitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //dark mode
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

        //Log out
        logout = findViewById(R.id.signOutButton);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);
                dialog.setTitle("Log out");
                dialog.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        Toast.makeText(HomeActivity.this, "Signed out", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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


        btntopitem = findViewById(R.id.btn_top_item);
        btntopitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SpotifyTopItemsActivity.class);
                startActivity(intent);
            }
        });


        btnViewSpotifyProfile = findViewById(R.id.btnViewSpotifyProfile);
        btnViewSpotifyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SpotifyUserProfileActivity.class);
                startActivity(intent);
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


        //drop down
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.drop_down_list,item);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(HomeActivity.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
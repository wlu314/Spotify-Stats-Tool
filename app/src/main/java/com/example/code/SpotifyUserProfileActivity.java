package com.example.code;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_user_profile);

        Intent intent = getIntent();
        String accessToken = null; // Local variable for accessToken
        if (intent != null && intent.hasExtra("ACCESS_TOKEN")) {
            accessToken = intent.getStringExtra("ACCESS_TOKEN");
        }

        if (accessToken != null) {
            fetchUserProfile(accessToken);
        } else {
            System.out.println("Token is null");
        }
    }

    private void fetchUserProfile(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me") // Profile endpoint
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    SpotifyUser user = gson.fromJson(responseData, SpotifyUser.class);
                    runOnUiThread(() -> {
                        TextView userIdTextView = findViewById(R.id.userIdTextView);
                        TextView userEmailTextView = findViewById(R.id.userEmailTextView);

                        userIdTextView.setText("accessToken" + accessToken);
                        userEmailTextView.setText("Email: " + user.email);
                    });
                } else {
                    // Log error or update UI with error message
                    runOnUiThread(() -> {
                        TextView userIdTextView = findViewById(R.id.userIdTextView);
                        userIdTextView.setText("Failed to fetch user data.");
                    });
                }
            }

        });
    }

    class SpotifyUser {
        String id;
        String email;
        // add more fields here based on the JSON response
    }
}

package com.example.code.SpotifyAPI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.code.R;
import com.example.code.ui.HomeActivity;
import com.example.code.ui.Notifications;
import com.example.code.ui.ProfileActivity;

import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyUserProfileActivity extends AppCompatActivity {
    ImageButton go_back_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        String accessToken = getIntent().getStringExtra("ACCESS_TOKEN");
        System.out.println("Access token obtained!" + accessToken);
        fetchUserProfile(accessToken);
    }

    private void fetchUserProfile(String accessToken) {
        System.out.println("user profile fetching");
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.spotify.com/v1/me")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();
            System.out.println("request made");

            try (Response response = client.newCall(request).execute()) {
                System.out.println( "Response code: " + response.code() + " - Message: " + response.message());
                if (!response.isSuccessful()) {
                    Log.d("SpotifyAPI", "Request failed: " + response.body().string());
                    return;
                }
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);

                String displayName = jsonObject.optString("display_name");
                System.out.println("name");
                String email = jsonObject.optString("email");
                System.out.println("email");
                String id = jsonObject.optString("id");
                System.out.println("id");
                String country = jsonObject.optString("country");
                System.out.println("country");
                String imageUrl = jsonObject.getJSONArray("images").getJSONObject(0).getString("url"); // Assuming user has at least one image
                runOnUiThread(() -> {
                    TextView displayNameView = findViewById(R.id.user_display_name);
                    displayNameView.setText(displayName);

                    TextView emailView = findViewById(R.id.user_email);
                    emailView.setText(email);

                    TextView idView = findViewById(R.id.user_id);
                    idView.setText(id);

                    TextView countryView = findViewById(R.id.user_country);
                    countryView.setText(country);

                    ImageView profileImageView = findViewById(R.id.user_profile_image);
                    Glide.with(this).load(imageUrl).into(profileImageView);
                });
            } catch (Exception e) {
                Log.e("SpotifyAPI", "Error fetching user profile", e);
            }
        }).start();
        System.out.println("completed");
    }

}

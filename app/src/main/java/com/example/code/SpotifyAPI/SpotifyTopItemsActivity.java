package com.example.code.SpotifyAPI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.code.R;
import com.example.code.User;
import com.example.code.ui.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyTopItemsActivity extends AppCompatActivity {
    ImageButton go_back_button;
    private DatabaseReference mDatabase;
    String UID;
    String token;
    HashMap<String, String> map;
    User user;
    ImageView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_items_activity);
        go_back_button = findViewById(R.id.go_back_button);
        go_back_button.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
        });
        UID = FirebaseAuth.getInstance().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        map = new HashMap<>();
        mDatabase.child("users").child(UID).child("token").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String accessToken = task.getResult().getValue(String.class);
                    Log.d("SpotifyAPI", "Access token obtained: " + accessToken);
                    fetchTopArtistsAndTracks(accessToken);
                }
            }
        });
    }

    private void fetchTopArtistsAndTracks(String accessToken) {
        new Thread(() -> {
            OkHttpClient client = new OkHttpClient();
            Request requestArtists = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/artists?limit=10")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            System.out.println(" top artist request made");

            Request requestTracks = new Request.Builder()
                    .url("https://api.spotify.com/v1/me/top/tracks?limit=10")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            try (Response response = client.newCall(requestArtists).execute()) {
                if (!response.isSuccessful()) {
                    Log.e("SpotifyAPI", "Failed to fetch top artists: " + response);
                    return;
                }
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray items = jsonObject.getJSONArray("items");
                System.out.println("Items got: "+ items);
                System.out.println("test");

                runOnUiThread(() -> {
                    try {
                        LinearLayout artistsContainer = findViewById(R.id.artists_container);
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject artist = items.getJSONObject(i);
                            String artistName = artist.getString("name");
                            System.out.println("testss");

                            JSONArray images = artist.getJSONArray("images");
                            String imageUrl = (images.length() > 0) ? images.getJSONObject(0).getString("url") : null;

                            TextView artistView = new TextView(SpotifyTopItemsActivity.this);
                            artistView.setText(artistName);
                            artistView.setTextSize(16);
                            artistView.setPadding(10, 10, 10, 10);

                            ImageView artistImageView = new ImageView(SpotifyTopItemsActivity.this);
                            if (imageUrl != null) {

                                Glide.with(SpotifyTopItemsActivity.this)
                                        .load(imageUrl)
                                        .into(artistImageView);
                            }
                            mainView = findViewById(R.id.imageView);
                            Glide.with(this).load(imageUrl).into(mainView);
                            //artistsContainer.addView(artistImageView);
                            //here
                            artistsContainer.addView(artistView);
                        }
                    } catch (Exception e) {
                        Log.e("SpotifyAPI", "Error parsing top artists", e);
                    }
                });
            } catch (Exception e) {
                Log.e("SpotifyAPI", "Error fetching top artists", e);
            }

            try (Response response = client.newCall(requestTracks).execute()) {
                if (!response.isSuccessful()) {
                    Log.e("SpotifyAPI", "Failed to fetch top tracks: " + response);
                    return;
                }
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray items = jsonObject.getJSONArray("items");

                runOnUiThread(() -> {
                    try {
                        LinearLayout tracksContainer = findViewById(R.id.tracks_container);
                        tracksContainer.removeAllViews();
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject track = items.getJSONObject(i);
                            String trackName = track.getString("name");
                            String trackURI = track.getString("uri"); // Extracting the URI

                            TextView trackView = new TextView(SpotifyTopItemsActivity.this);
                            //trackView.setText("Name: " + trackName + "\nURI: " + trackURI);
                            trackView.setText(trackName);
                            trackView.setTextSize(16);
                            trackView.setPadding(10, 10, 10, 10);
                            tracksContainer.addView(trackView);
                        }
                    } catch (Exception e) {
                        Log.e("SpotifyAPI", "Error parsing top tracks", e);
                    }
                });
            } catch (Exception e) {
                Log.e("SpotifyAPI", "Error fetching top tracks", e);
            }
        }).start();
    }


}

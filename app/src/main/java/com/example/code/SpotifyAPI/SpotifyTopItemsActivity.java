package com.example.code.SpotifyAPI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_items_activity);
        go_back_button = findViewById(R.id.go_back_button1);
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
            try (Response response = client.newCall(requestArtists).execute()) {
                if (!response.isSuccessful()) {
                    Log.e("SpotifyAPI", "Failed to fetch top artists: " + response);
                    return;
                }
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray items = jsonObject.getJSONArray("items");
                System.out.println("Items got: "+ items);

                runOnUiThread(() -> {
                    try {
                        TextView topArtistNameView = findViewById(R.id.top_artist_name);
                        if (items.length() > 0) {
                            JSONObject firstArtist = items.getJSONObject(0);
                            topArtistNameView.setText(firstArtist.getString("name"));
                        }
                    } catch (Exception e) {
                        Log.e("SpotifyAPI", "Error parsing top artists", e);
                    }
                });
            } catch (Exception e) {
                Log.e("SpotifyAPI", "Error fetching top artists", e);
            }
        }).start();
    }
}

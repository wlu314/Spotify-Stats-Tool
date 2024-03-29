package com.example.code.SpotifyAPI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.code.R;
import com.example.code.User;
import com.example.code.ui.HomeActivity;
import com.example.code.ui.Notifications;
import com.example.code.ui.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyUserProfileActivity extends AppCompatActivity {
    ImageButton go_back_button;
    private DatabaseReference mDatabase;
    String UID;
    String token;
    HashMap<String, String> map;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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
                if (!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    String accessToken = task.getResult().getValue().toString();
                    System.out.println("Access token obtained!" + accessToken);
                    fetchUserProfile(accessToken);
                }
            }
        });
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
                System.out.println("name"+displayName);
                String email = jsonObject.optString("email");
                System.out.println("email"+email);
                String id = jsonObject.optString("id");
                System.out.println("id"+id);
                String country = jsonObject.optString("country");
                System.out.println("country"+country);
                String imageUrl = jsonObject.getJSONArray("images").getJSONObject(0).getString("url"); // Assuming user has at least one image

                //add to DB
                user = new User();
                user.name = displayName;
                user.email = email;
                user.id = id;
                user.country = country;
                HashMap<String, Object> map = new HashMap<>();
                map.put("name",displayName);
                map.put("email",email);
                map.put("id",id);
                map.put("country",country);
                mDatabase.child("users").child(UID).updateChildren(map);

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

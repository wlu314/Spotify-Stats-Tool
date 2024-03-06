package com.example.code;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyAPIConnection extends AppCompatActivity {
    // Request code is used to verify if result comes from the login activity. can be set to anything
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "0d33de8c5b634aadbe0250636cd2e0aa";
    private static final String REDIRECT_URI = "spotify-api://redirect/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        //Once Button is click, login is initiated
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> initiateLogin());
    }

    private void initiateLogin() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginInBrowser(this, request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthorizationResponse response = AuthorizationResponse.fromUri(uri);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response
                    String accessToken = response.getAccessToken();

                    // Fetch User Profile
                    fetchUserInformation(accessToken);

                    //Navigate to the Main Menu
                    Intent mainMenu = new Intent(this, Statistics.class);
                    startActivity(mainMenu);
                    finish();
                    break;
                // Auth flow returned an error
                case ERROR:
                    try {
                        throw new Exception("There was an error connecting to Spotify, please try again!");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                // Most likely auth flow was cancelled
                default:
                    //Send back to the Spotify Connection screen
                    Intent backtToConnection = new Intent(this, SpotifyAPIConnection.class);
                    startActivity(backtToConnection);
                    finish();
            }
        }
    }
    private void fetchUserInformation(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        String userProfileEndpoint = "https://api.spotify.com/v1/me";
        Request request = new Request.Builder()
                .url(userProfileEndpoint)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // TODO parse the data and store it in SQLite
                    // TODO call a method to store the parsed data in SQLite
                } else {
                    try {
                        throw new Exception("The user profile cannot be displayed due to an error.");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
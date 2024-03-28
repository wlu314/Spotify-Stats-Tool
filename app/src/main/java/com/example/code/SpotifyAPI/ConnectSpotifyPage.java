package com.example.code.SpotifyAPI;

import static android.app.PendingIntent.getActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.code.R;
import com.example.code.ui.HomeActivity;
import com.example.code.ui.Statistics;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class ConnectSpotifyPage extends AppCompatActivity{
    private static final String CLIENT_ID = "5de169cf540a4cd5aea89d26639f2cf0";
    private static final String REDIRECT_URI = "spotify-api://redirect/";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> openBrowser());
    }

    /**
     * Opens browser to allow user to login and get authorization code.
     */
    public void openBrowser() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[] {"user-read-email", "user-read-private","streaming"});
        builder.setShowDialog(true);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginInBrowser(this, request);
    }

    /**
     * Uri response gets the authorization response which is the access token.
     * @param intent The new intent that was started from the openBrowser() method
     *
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthorizationResponse response = AuthorizationResponse.fromUri(uri);
            switch(response.getType()) {
                case TOKEN:
                    System.out.println("Success! This is the token " + response.getAccessToken());
                    LoginSuccess(response);
                    break;
                case ERROR:
                    System.out.println("Bad Request.");
                    break;
                default:
                    System.out.println("Unexpected response code.");
            }
        }
    }

    private void LoginSuccess(AuthorizationResponse response) {
        Intent intent = new Intent(this, SpotifyUserProfileActivity.class);
        intent.putExtra("ACCESS_TOKEN", response.getAccessToken()); // Pass the access token
        startActivity(intent);
        finish(); // Close this activity
    }

}

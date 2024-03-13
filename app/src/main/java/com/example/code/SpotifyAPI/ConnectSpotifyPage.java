package com.example.code.SpotifyAPI;

import static android.app.PendingIntent.getActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.code.R;
import com.example.code.ui.Statistics;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class ConnectSpotifyPage extends AppCompatActivity{
    private static final String CLIENT_ID = "0d33de8c5b634aadbe0250636cd2e0aa";
    private static final String REDIRECT_URI = "spotify-api://redirect/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> openBrowser());
    }

    /**
     * Allows user to login to get an authorization code after the URI query
     */
    public void openBrowser() {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[] {"streaming"});
        builder.setShowDialog(true);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginInBrowser(this, request);
    }

    /**
     * Process result from openBrowser().
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
                    System.out.println("Success! This is the token" + response);
                    String accessToken = response.getAccessToken();
                    SpotifyUserInfo.fetchUserProfile(accessToken);
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
        Intent mainMenu = new Intent(this, Statistics.class);
        startActivity(mainMenu);
        finish();
    }
}

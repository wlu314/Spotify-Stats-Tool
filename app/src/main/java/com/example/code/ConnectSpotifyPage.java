package com.example.code;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.code.SpotifyAPI.SpotifyOauth2Application;
import com.example.code.ui.HomeActivity;


public class ConnectSpotifyPage extends AppCompatActivity implements SpotifyOauth2Application.SpotifyAuthenticationListener{
    String accessToken;
    private SpotifyOauth2Application spotifyHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        spotifyHandler = new SpotifyOauth2Application(this, this);
        //Once Button is click, login is initiated
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> spotifyHandler.initiateLogin());


    }

    /**
     * After Spotify login flow (authenticates user), spotify redirect user back to application
     * using the URI. This triggers onNewIntent()
     * @param intent The new intent that was started for the activity.
     *
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        spotifyHandler.handleIntent(intent);
    }

    /**
     * When there is a token received it will open up the Statistics Page.
     * The spotify handler will generate all neccesary code and store it within the firebase db
     * @param accessToken This is the access token.
     */
    @Override
    public void onTokenReceived(String accessToken) {
        // Once the token is received
        spotifyHandler.fetchUserInformation(accessToken);
        Log.d("ConnectSpotifyPage", "Access token received, starting Statistics activity.");
        //store accessToken
        String token = accessToken;
        Context context;

        Intent mainMenu = new Intent(this, HomeActivity.class);
        startActivity(mainMenu);
        finish();
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }
}

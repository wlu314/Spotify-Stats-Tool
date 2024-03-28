package com.example.code.SpotifyAPI;

import static android.app.PendingIntent.getActivity;

import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.code.R;
import com.example.code.ui.HomeActivity;
import com.example.code.ui.Statistics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class ConnectSpotifyPage extends AppCompatActivity{
    private static final String CLIENT_ID = "18361388d1984e72932933a3e37aa877";
    private static final String REDIRECT_URI = "spotify-api://redirect/";
    private DatabaseReference mDatabase;
    String UID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> openBrowser());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = FirebaseAuth.getInstance().getUid();
    }

    /**
     * Opens browser to allow user to login and get authorization code.
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
                    //store to DB , token and UserID
                    writeNewUser(response.getAccessToken(),UID);
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

    private void writeNewUser(String token, String userId) {
        User user = new User(token, userId);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private Object getToken(){
        //returning as User Object
        return mDatabase.child("users").child(UID).get().getResult().getValue();
    }
}

package com.example.code.SpotifyAPI;


import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.code.R;
import com.example.code.User;
import com.example.code.ui.Statistics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class ConnectSpotifyPage extends AppCompatActivity{
    private static final String CLIENT_ID = "0d33de8c5b634aadbe0250636cd2e0aa";
    private static final String REDIRECT_URI = "spotify-api://redirect/";
    private DatabaseReference mDatabase;
    String UID;

    private SharedPreferences.Editor editor;
    private SharedPreferences msharedPreferences;

    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotifyapi);
        findViewById(R.id.spotify_login_btn).setOnClickListener(v -> openBrowser());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = FirebaseAuth.getInstance().getUid();

        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
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
                    // Store access token in shared preferences
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    editor.apply();
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


    private void writeNewUser(String token, String userId) {
        User user = new User(token, userId);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private Object getToken(){
        //returning as User Object
        return mDatabase.child("users").child(UID).get().getResult().getValue();
    }

    private void waitForUserInfo() {
        UserEndpoints userService = new UserEndpoints(queue, msharedPreferences);
        userService.get(() -> {
            com.example.code.Models.User user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            editor.commit();
        });
    }

}

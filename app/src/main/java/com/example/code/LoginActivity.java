package com.example.code;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private final String clientId = "5de169cf540a4cd5aea89d26639f2cf0";
    private final String redirectUri = "http://localhost3000";
    private final String codeVerifier = "ApHwl00IkDlUSv9mIMDlnoMvV2OSKDj4m65H2KNWQr4";
    private String codeChallenge = "ZV80ShAYnUr5JDmoqXiz_QhpsR1Q7QuW7GDU_mCjqRg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String url = "https://accounts.spotify.com/authorize?" +
                "client_id=" + clientId +
                "&response_type=code" +
                "&redirect_uri=" + redirectUri +
                "&code_challenge_method=S256" +
                "&code_challenge=" + codeChallenge +
                "&scope=user-read-private user-read-email";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}

package com.example.code;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

public class CallbackActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private final String clientId = "5de169cf540a4cd5aea89d26639f2cf0";
    private final String redirectUri = "http://localhost3000";
    private final String codeVerifier = "ApHwl00IkDlUSv9mIMDlnoMvV2OSKDj4m65H2KNWQr4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_callback); // If you have a layout for this activity

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                exchangeCodeForToken(code);
            } else {
                // Handle error or denial here
                Log.e("CallbackActivity", "Authorization code not found. Check if the user denied the authorization.");
            }
        }
    }

    private void exchangeCodeForToken(String code) {
        RequestBody body = new FormBody.Builder()
                .add("client_id", clientId)
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUri)
                .add("code_verifier", codeVerifier)
                .build();

        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure
                Log.e("CallbackActivity", "Token request failed: " + e.getMessage(), e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d("CallbackActivity", "Token Response: " + responseData);
                    // Parse the JSON response to extract the access token and other data
                } else {
                    Log.e("CallbackActivity", "Token request was not successful. Response: " + response.toString());
                }
            }
        });
    }
}


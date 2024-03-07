package com.example.code.SpotifyAPI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class SpotifyOauth2Application {
    //When Development is finished these should be deleted as it's hardcoded and not secure
    private static final String CLIENT_ID = "0d33de8c5b634aadbe0250636cd2e0aa";
    private static final String REDIRECT_URI = "spotify-api://redirect/";
    private static final int REQUEST_CODE = 1337;
    private Context context;
    private SpotifyAuthenticationListener listener;

    public interface SpotifyAuthenticationListener {
        void onTokenReceived(String accessToken);
        void onError(String errorMessage);
    }
    public SpotifyOauth2Application(Context context, SpotifyAuthenticationListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void initiateLogin() {
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
                .setScopes(new String[]{"streaming"});
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginInBrowser((Activity) context, request);
    }

    /**
     * Processes an incoming Intent to handle Spotify authentication responses. This method extracts the URI data from the intent,
     * which contains the result of the authentication attempt. It then parses this data to determine the outcome of the authentication process,
     * whether it's a successful token acquisition, an error, or another status (such as cancellation by the user).
     * On successful authentication, it retrieves the access token from the response and notifies the registered listener by calling
     * {@code onTokenReceived(String accessToken)}. If an error occurs or the authentication process is cancelled,
     * it notifies the listener of the error or cancellation through {@code onError(String errorMessage)}.
     *
     * @param intent The incoming Intent from which the method extracts the URI containing the Spotify authentication response.
     */

    public void handleIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            AuthorizationResponse response = AuthorizationResponse.fromUri(uri);
            switch (response.getType()) {
                case TOKEN:
                    String accessToken = response.getAccessToken();
                    requestRefreshToken(accessToken);
                    if (listener != null) {
                        listener.onTokenReceived(accessToken);
                    }
                    break;
                case ERROR:
                    if (listener != null) {
                        listener.onError("There was an error connecting to Spotify, please try again!");
                    }
                    break;
                default:
                    if (listener != null) {
                        listener.onError("Authentication cancelled.");
                    }
            }
        }
    }

    /**
     * If the user accepts the request, the app is ready to exchange the authorization code for an access token.
     * YOu can send a POST request to the "/api/token" endpoint
     * @param authorizationCode This is the access token from the authentication request
     */
    public void requestRefreshToken(String authorizationCode) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", authorizationCode)
                .add("redirect_uri", REDIRECT_URI)
                .add("client_id", CLIENT_ID)
                .build();
        Request request = new Request.Builder()
                .url("https://accounts.spotify.com/api/token")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    // Extract the access and refresh tokens from the response
                    // Assume the response is a JSON object and parse accordingly
                    try {
                        JSONObject json = new JSONObject(responseBody);
                        String accessToken = json.getString("access_token");
                        String refreshToken = json.getString("refresh_token");
                        // Store the refresh token for later use
                        System.out.println("The refresh token: " + refreshToken + " has been stored.");
                        storeRefreshToken(refreshToken);
                        // Notify the listener that a new access token was received
                        if(listener != null) {
                            listener.onTokenReceived(accessToken);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(listener != null) {
                            listener.onError("Failed to parse token response.");
                        }
                    }
                } else {
                    // Handle error
                    if(listener != null) {
                        listener.onError("Failed to get access token.");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // Handle failure
                if(listener != null) {
                    listener.onError("Request to get access token failed.");
                }
            }
        });
    }

    private void storeRefreshToken(String refreshToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SpotifyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refresh_token", refreshToken);
        editor.apply();
    }
    private String getStoredRefreshToken() {
        SharedPreferences sp = context.getSharedPreferences("SpotifyPreferences", Context.MODE_PRIVATE);
        return sp.getString("refresh_token", null);
    }


    /**
     * I need to request authorization which then I will exchange for an access token
     * @param accessToken The access code given as a string.
     */
    public void fetchUserInformation(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        String userProfileEndpoint = "https://api.spotify.com/v1/me";
        Request request = new Request.Builder()
                .url(userProfileEndpoint)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        client.newCall(request).enqueue(new Callback() {
            /**
             * If the listener is null it will give an error.
             */
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (listener != null) {
                    listener.onError("Failed to fetch user information.");
                }
            }

            /**
             * First checks if the response is successful.
             */
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    // TODO parse the data and store it in SQLite
                    System.out.println(responseData);
                } else {
                    if (listener != null) {
                        listener.onError("The user profile cannot be displayed due to an error.");
                    }
                }
            }
        });
    }
}

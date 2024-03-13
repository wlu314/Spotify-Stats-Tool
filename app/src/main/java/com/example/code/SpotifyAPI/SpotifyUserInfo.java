package com.example.code.SpotifyAPI;

import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpotifyUserInfo {
    private static final String BASE_URL = "https://api.spotify.com";

    /**
     * Fetches the user's Spotify profile information.
     * @param accessToken The access token obtained from the authorization process.
     */
    public static void fetchUserProfile(String accessToken) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(BASE_URL + "/me");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Set the Authorization header with the access token
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                System.out.println("User Profile: " + jsonResponse.toString());
            } else {
                System.out.println("Failed to fetch user profile. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SpotifyUserInfo", "Error occurred while fetching user profile.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

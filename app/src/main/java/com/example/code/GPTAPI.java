package com.example.code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import android.util.Log;  // Ensure to import Log for Android debugging

public class GPTAPI {
    private static final String API_KEY = "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\n"; // Replace with your actual API key and ensure it's kept secure
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public interface Callback {
        void onSuccess(String response);
        void onError(Exception error);
    }

    public static void chatGPT(String prompt, Callback callback) {
        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);

                // Prepare the JSON body with the messages array
                JsonObject jsonBody = new JsonObject();
                jsonBody.addProperty("model", "gpt-3.5-turbo");

                JsonArray messages = new JsonArray();
                JsonObject firstMessage = new JsonObject();
                firstMessage.addProperty("role", "user");
                firstMessage.addProperty("content", prompt);
                messages.add(firstMessage);

                jsonBody.add("messages", messages); // Include the messages array
                jsonBody.addProperty("max_tokens", 100);

                // Convert the JSON body to string and send the request
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // Handle the response
                int responseCode = connection.getResponseCode();
                StringBuilder response = new StringBuilder();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                    }
                    callback.onSuccess(response.toString());
                } else {
                    // Handle non-OK responses and read the error stream
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line.trim());
                        }
                    }
                    callback.onError(new Exception("Failed: HTTP error code: " + responseCode + " - " + response.toString()));
                }
            } catch (Exception e) {
                Log.e("API Request Error", "Error during API request", e);
                callback.onError(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}

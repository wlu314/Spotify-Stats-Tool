package com.example.code.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.code.GPTAPI;
import com.example.code.R;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class LLMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llm);

        TextView resultTextView = findViewById(R.id.result_text_view);
        String[] songs1 = {"Blinding Lights - The Weeknd", "Good 4 U - Olivia Rodrigo", "Levitating - Dua Lipa"};
        String[] songs2 = {"Shape of You - Ed Sheeran", "Someone Like You - Adele", "Uptown Funk - Mark Ronson ft. Bruno Mars"};
        String[] songs3 = {"Rolling in the Deep - Adele", "Firework - Katy Perry", "Happy - Pharrell Williams"};
        String[] songs4 = {"Sorry - Justin Bieber", "Hello - Adele", "Roar - Katy Perry"};
        String[] songs5 = {"Smells Like Teen Spirit - Nirvana", "Enter Sandman - Metallica", "Creep - Radiohead"};

        String prompt = "Based on someone's Spotify Wrapped featuring songs such as " +
                String.join(", ", songs1) + ", in less than 400 characters, describe how their musical taste might reflect their way of thinking, acting, or dressing.";


        GPTAPI.chatGPT(prompt, new GPTAPI.Callback() {
            @Override
            public void onSuccess(String response) {
                // Parse the response and update the TextView on the UI thread
                String readableMessage = getReadableMessageFromResponse(response);
                runOnUiThread(() -> resultTextView.setText(readableMessage));
            }

            @Override
            public void onError(Exception error) {
                // Display error message on the UI thread
                runOnUiThread(() -> resultTextView.setText("Error: " + error.getMessage()));
            }
        });
    }

    private String getReadableMessageFromResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray choices = jsonObject.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject choice = choices.getJSONObject(0);
                String message = choice.getJSONObject("message").getString("content");
                // Replace escaped newlines and quotes with actual newlines and quotes
                return message.replace("\\n", "\n").replace("\\\"", "\"");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error parsing response: " + e.getMessage();
        }
        return "No content found in response.";
    }
}

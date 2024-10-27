# Spotify Wrapped Stats

This app provides a personalized, interactive experience of your Spotify music listening habits. By logging into your Spotify account, you can access a colorful and detailed presentation of your music tastes. Built to capture the spirit of Spotify's annual "Wrapped" feature, this app offers flexibility, customization, and social sharing options to create a fun and engaging experience year-round.

## Features

### Personalized Stats Page
- View creative, detailed summaries of your listening habits, generated from your Spotify account data.  
<img width="182" alt="Screenshot 2024-10-27 at 4 11 23 PM" src="https://github.com/user-attachments/assets/2de31a36-5726-4e7a-9ca5-d1eed76ec0f9">

### Customizable Experience
- Utilize an LLM API to dynamically describe the lifestyle and preferences associated with your music taste. Compare this personalized insight with a friend's during a Duo-Wrapped session.  

### Account Management and User Preferences
- Customize your experience with options like toggling dark mode and changing notification preferences. Manage your account, save past Spotify Wrapped summaries, update login information, or delete your account if needed.

<img width="774" alt="Screenshot 2024-10-27 at 4 10 53 PM" src="https://github.com/user-attachments/assets/7b59d0d0-5051-43ec-a30c-8299a9e03fe0">

### Friends List
- Search for friends and view what they’re currently listening to. Profiles are sorted alphabetically, and you can view a friend’s top tracks by tapping on their profile.  

<img width="476" alt="Screenshot 2024-10-27 at 4 10 32 PM" src="https://github.com/user-attachments/assets/ba02c99e-24fe-464e-83fa-02f413aef3d4">

### Game Mode
- Play a fun quiz game where you identify artists based on their images, extracted from the Spotify API. Compete against the clock with each question timed to 10 seconds, and track your high scores.

<img width="885" alt="Screenshot 2024-10-27 at 4 10 14 PM" src="https://github.com/user-attachments/assets/3cea7650-1755-443c-8111-f654889e3c9d">


## Getting Started

To run this app locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/username/repository-name.git
   ```

2. **Open the Project in Android Studio:**
   - Open **Android Studio**.
   - Select **File > Open**.
   - Navigate to the project directory and select it to open.

3. **Install Dependencies:**
   - Android Studio should automatically sync and install necessary dependencies. If not, go to **File > Sync Project with Gradle Files**.

4. **Set Up Spotify API Credentials:**
   - Create a `local.properties` file in the root of the project (if it doesn’t already exist).
   - Add your Spotify API credentials to a `.env` file (or directly in your code if configured) and reference it appropriately in your codebase.
     ```
     SPOTIFY_CLIENT_ID=your_client_id
     SPOTIFY_CLIENT_SECRET=your_client_secret
     ```

5. **Run the App:**
   - Select an Android emulator or connect a physical device.
   - Click the **Run** button (green play icon) in the toolbar, or select **Run > Run 'app'** from the menu.
   - Android Studio will build and deploy the app to your selected device or emulator.

6. **Login with Spotify:**
   - Once the app is running on your device, you can log in with your Spotify account to explore the features.
---

## Contributing

If you'd like to contribute, feel free to open a pull request. We welcome bug fixes, feature requests, and other improvements.

*all designs are done in figma*


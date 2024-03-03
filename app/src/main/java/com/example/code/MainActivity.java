package com.example.code;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.code.databinding.ActivityMainBinding;
import com.example.code.ui.friends.FriendsFragment;
import com.example.code.ui.game.GameFragment;
import com.example.code.ui.home.HomeFragment;
import com.example.code.ui.stats.StatsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {




    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        loadFragment(new HomeFragment());

        /**
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_stat, R.id.navigation_friends, R.id.navigation_game)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        **/
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.navigation_home) {
                    fragment = new HomeFragment();
                } else if (item.getItemId() == R.id.navigation_stat) {
                    fragment = new StatsFragment();
                } else if (item.getItemId() == R.id.navigation_friends) {
                    fragment = new FriendsFragment();
                } else if (item.getItemId() == R.id.navigation_game) {
                    fragment = new GameFragment();
                }
                return loadFragment(fragment);
            }
        });



        //Add your Spotify API call here
        //fetchSpotifyUserProfile();
    }

    private void fetchSpotifyUserProfile() {
        String accessToken = "YOUR_ACCESS_TOKEN"; // Replace with actual access token
        SpotifyService service = SpotifyApi.getClient().create(SpotifyService.class);
        Call<UserProfile> call = service.getCurrentUserProfile("Bearer " + accessToken);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    UserProfile userProfile = response.body();
                    Log.d("MainActivity", "User Profile Name: " + userProfile.getDisplayName());
                    // update your UI based on the Spotify user profile data
                } else {
                    Log.e("MainActivity", "Error fetching Spotify user profile");
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Log.e("MainActivity", "Spotify API call failed", t);
            }
        });
    }




    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            return true;
        }
        return false;
    }

}

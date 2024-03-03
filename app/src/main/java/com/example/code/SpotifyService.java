package com.example.code;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;



public interface SpotifyService {
    @GET("v1/me")
    Call<UserProfile> getCurrentUserProfile(@Header("Authorization") String authToken);
}

package com.example.code;

public class User {

    public String token;
    public String fireBaseUID;
    public String name,email,id,country;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String token, String fireBaseUID) {
        this.token = token;
        this.fireBaseUID = fireBaseUID;
    }
}

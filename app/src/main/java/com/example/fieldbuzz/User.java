package com.example.fieldbuzz;

public class User {
    private String token;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getToken() {
        return token;
    }
}

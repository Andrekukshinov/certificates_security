package com.epam.esm.web.model;

public class AuthenticationCredentials {
    private String username;
    private String password;

    public AuthenticationCredentials() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
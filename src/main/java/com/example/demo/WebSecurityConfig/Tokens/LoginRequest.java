package com.example.demo.WebSecurityConfig.Tokens;

import org.springframework.stereotype.Component;

@Component
public class LoginRequest {
    private String username;
    private String password;
    private boolean rememberMe;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


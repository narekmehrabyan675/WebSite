package com.example.demo.WebSecurityConfig.Tokens;

import jdk.jfr.DataAmount;
import lombok.Data;
import org.springframework.stereotype.Component;
@Data
public class JwtResponse {
    private String token;
    private String tokenType = "Bearer ";

    public JwtResponse(String token) {
        this.token = token;
    }

}


package com.example.demo.Controllers;

import com.example.demo.WebSecurityConfig.Tokens.JwtGenerator;
import com.example.demo.WebSecurityConfig.Tokens.JwtResponse;
import com.example.demo.WebSecurityConfig.Tokens.LoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        logger.info("Received authentication request for user: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            boolean rememberMe = loginRequest.isRememberMe();
            String jwt = jwtGenerator.generateToken(authentication, rememberMe);

            logger.info("Generated JWT token for user: {}", loginRequest.getUsername());

            // Set JWT в cookies
            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(rememberMe ? 1209600 : 86400); // 2 недели или 1 день
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            logger.info("RememberMe: {}", rememberMe);

            return new ResponseEntity<>(new JwtResponse(jwt), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtGenerator jwtGenerator;

    private String recaptchaSecretKey = "6Ldg_IoqAAAAAFJtl-2nLEu79Qrjn_nWzNZe1kfs";

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        logger.info("Received authentication request for user: {}", loginRequest.getUsername());

        // Проверка reCAPTCHA токена
        if (loginRequest.getRecaptchaToken() == null || !validateRecaptcha(loginRequest.getRecaptchaToken())) {
            logger.error("Invalid or missing reCAPTCHA token for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Генерация JWT токена
            boolean rememberMe = loginRequest.isRememberMe();
            String jwt = jwtGenerator.generateToken(authentication, rememberMe);

            // Установка JWT токена в куки
            Cookie jwtCookie = new Cookie("JWT_TOKEN", jwt);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setMaxAge(rememberMe ? 1209600 : 86400); // 14 дней или 1 день
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean validateRecaptcha(String recaptchaToken) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecretKey);
        params.add("response", recaptchaToken);

        try {
            logger.info("Sending reCAPTCHA validation request to Google. Token: {}", recaptchaToken);

            Map<String, Object> response = restTemplate.postForObject(RECAPTCHA_VERIFY_URL, params, Map.class);

            logger.info("Received reCAPTCHA response from Google: {}", response);

            // Проверяем только поле "success"
            boolean success = (Boolean) response.get("success");
            logger.info("reCAPTCHA validation success: {}", success);

            return success; // Успех проверки
        } catch (Exception e) {
            logger.error("Error during reCAPTCHA validation", e);
            return false;
        }
    }

}

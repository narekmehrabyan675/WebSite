package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecaptchaService {
    @Value("${recaptcha.secret}")
    private String secret;

    @Value("${recaptcha.verify.url}")
    private String recaptchaVerifyUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean verify(String recaptchaResponse) {
        Map<String, String> params = new HashMap<>();
        params.put("secret", secret);
        params.put("response", recaptchaResponse);

        Map response = restTemplate.postForObject(recaptchaVerifyUrl, params, Map.class);
        return response != null && Boolean.TRUE.equals(response.get("success"));
    }
}


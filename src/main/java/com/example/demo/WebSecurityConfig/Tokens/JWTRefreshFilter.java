package com.example.demo.WebSecurityConfig.Tokens;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTRefreshFilter extends OncePerRequestFilter {

    private final JwtGenerator tokenGenerator;

    @Autowired
    public JWTRefreshFilter(JwtGenerator tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем текущий JWT из запроса
            String currentJwt = getJwtFromRequest(request);

            // Проверяем, валиден ли текущий JWT
            if (tokenGenerator.validateToken(currentJwt)) {
                // Генерируем новый JWT и устанавливаем его в cookie
                String newToken = tokenGenerator.generateToken(authentication, true); // Учитываем rememberMe

                Cookie cookie = new Cookie("rememberme", newToken);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(1209600); // 2 недели
                response.addCookie(cookie);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberme".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

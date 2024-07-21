package com.example.demo.WebSecurityConfig;

import com.example.demo.Services.UserDetailsServiceImpl;
import com.example.demo.WebSecurityConfig.Tokens.JWTRefreshFilter;
import com.example.demo.WebSecurityConfig.Tokens.JwtAuthEntryPoint;
import com.example.demo.WebSecurityConfig.Tokens.JwtGenerator;
import com.example.demo.WebSecurityConfig.Tokens.JWTAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JWTRefreshFilter jwtRefreshFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder,
                             JwtAuthEntryPoint jwtAuthEntryPoint, JwtGenerator jwtGenerator , JWTRefreshFilter jwtRefreshFilter) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtGenerator = jwtGenerator;
        this.jwtRefreshFilter = jwtRefreshFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/token").permitAll()

                        .requestMatchers("/", "/reg/registration", "/user", "/activate/**" , "/auth/**", "user/get", "/favicon.ico" , "/error").permitAll()
                        .requestMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR")
                        .requestMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
                        .requestMatchers("/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .deleteCookies("JWT_TOKEN")
                        .permitAll()
                )
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtRefreshFilter, JWTAuthenticationFilter.class);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtGenerator, userDetailsServiceImpl);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SimpleUrlAuthenticationSuccessHandler successHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/home/welcome");
    }
}

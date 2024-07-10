package com.example.demo.Services;

import com.example.demo.MySQL.MyUserDetail;
import com.example.demo.MySQL.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GetCurrentUser {
    public User getCurrentUserDeitals(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the user details
        Object principal = authentication.getPrincipal();
        return ((User)principal);
    }

    public String getCurrentUsername() {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the user details
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return username;
    }
}
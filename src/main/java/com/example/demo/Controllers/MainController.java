package com.example.demo.Controllers;

import com.example.demo.MySQL.User;
import com.example.demo.Services.GetCurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // This means that this class is a Controller
@RequestMapping(path="/home") // This means URL's start with /demo (after Application path)
public class MainController {
    @GetMapping("/welcome")
    public String homePage(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, Model model) {
        // Get the user details from SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.getPrincipal() instanceof UserDetails) ? ((UserDetails) authentication.getPrincipal()).getUsername() : "Guest";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("role", userDetails.getAuthorities().toString());
        }
        // Add the username to the model to display in the view
        model.addAttribute("username", username);

        return "greeting";
    }

    @PostMapping("/welcome")
    public String homePagePost(Model model){
        // Get the user details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.getPrincipal() instanceof UserDetails) ? ((UserDetails) authentication.getPrincipal()).getUsername() : "Guest";
        // Add the username to the model to display in the view
        model.addAttribute("username", username);
        return "greeting"; // Return the name of the home view (home.html)
    }
}

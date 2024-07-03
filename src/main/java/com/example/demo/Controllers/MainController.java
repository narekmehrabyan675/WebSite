package com.example.demo.Controllers;

import com.example.demo.MySQL.User;
import com.example.demo.Services.GetCurrentUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // This means that this class is a Controller
@RequestMapping(path="/home") // This means URL's start with /demo (after Application path)
public class MainController {
    @GetMapping("/welcome")
    public String homePage(Model model){
        return "greeting";
    }
    @PostMapping("/welcome")
    public String homePagePost(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the user details
        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof User) {
            username = ((User) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // Add the username to the model to display in the view
        model.addAttribute("username", username);
        return "greeting"; // Return the name of the home view (home.html)
    }
}

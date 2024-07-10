package com.example.demo.Controllers.Registration;

import com.example.demo.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class Activation {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/activate/{activationcode}")
    public String userGetActivation(@PathVariable("activationcode") String activationcode, Model model) {
        boolean isActivated = userDetailsService.activateUser(activationcode);
        model.addAttribute("isActivated", isActivated);
        model.addAttribute("activationcode", activationcode);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.getPrincipal() instanceof UserDetails) ? ((UserDetails) authentication.getPrincipal()).getUsername() : "Guest";
        model.addAttribute("username", username);

        return "activate";
    }

    @PostMapping("/activate/{activationcode}")
    public String userPostActivation(@PathVariable("activationcode") String activationcode, Model model) {
        boolean isActivated = userDetailsService.activateUser(activationcode);
        model.addAttribute("isActivated", isActivated);
        model.addAttribute("activationcode", activationcode);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated!");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null && authentication.getPrincipal() instanceof UserDetails) ? ((UserDetails) authentication.getPrincipal()).getUsername() : "Guest";
        model.addAttribute("username", username);

        return "activate";
    }
}

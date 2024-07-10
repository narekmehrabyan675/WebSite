package com.example.demo.Controllers.Registration;

import com.example.demo.MySQL.Role;
import com.example.demo.MySQL.User;
import com.example.demo.Services.UserDetailsServiceImpl;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path="/reg")
public class ControlerForRegister {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @GetMapping("/registration")
    public String loginGet(Model model){
        return "registration";
    }
    @PostMapping(path = "/registration")
    public @ResponseBody String loginIntoWebsite(@ModelAttribute User user,
                                                 Model model){

        boolean b = userDetailsService.addUser(user);
        model.addAttribute("user",user);
        if(!b){
            return "Login is already taken!!!";
        }
        return ("User registered successfully!!!");
    }
}
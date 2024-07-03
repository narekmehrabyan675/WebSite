package com.example.demo;

import com.example.demo.MySQL.User;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // This means that this class is a Controller
@RequestMapping(path="/reg") // This means URL's start with /demo (after Application path)
public class ControlerForRegister {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String loginGet(Model model){
        return "registration";
    }
    @PostMapping(path = "/registration")
    public @ResponseBody String loginIntoWebsite(@RequestParam String username,
                                                 @RequestParam String password,
                                                 //@RequestParam Integer id,
                                                 Model model){
        User n = new User();
        n.setUsername(username);
        n.setPassword(password);
        //n.setId(id);
        if(userRepository.getUserByUsername(username)!=null){
            return "Login is already taken!!!";
        }
        model.addAttribute("user",n);
        userRepository.save(n);
        return ("User registered successfully!!!");
    }
}
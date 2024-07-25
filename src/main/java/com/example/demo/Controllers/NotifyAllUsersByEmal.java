package com.example.demo.Controllers;

import com.example.demo.MySQL.User;
import com.example.demo.Services.MailSenderService;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller()
@RequestMapping("/notify")
public class NotifyAllUsersByEmal {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @GetMapping("/byemail")
    public String byEmailGet(Model model) {
        return "notify-all-users-by-email";
    }

    @PostMapping("/byemail")
    public String byEmailPost(@RequestParam String message, Model model) {
        model.addAttribute("message", message);
        Iterable<User> allusers = userRepository.findAll();
        for (User user : allusers) {
            if(user.getEmail() == null){
                continue;
            }
            String format = String.format("Dear %s, \n%s", user.getUsername(), message);
            mailSenderService.send(user.getEmail() , "Notification" , format);
        }
            return "redirect:/notify/byemail";
    }
}

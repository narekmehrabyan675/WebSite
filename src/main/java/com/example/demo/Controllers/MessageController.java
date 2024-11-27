package com.example.demo.Controllers;

import com.example.demo.MongoDB.Message;
import com.example.demo.MySQL.User;
import com.example.demo.Services.MessageService;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/send")
    public String showSendMessageForm(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "send-message";
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String recipientUsername, @RequestParam String content, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderUsername = authentication.getName();

        User sender = userRepository.getUserByUsername(senderUsername);
        User recipient = userRepository.getUserByUsername(recipientUsername);

        if (sender == null || recipient == null) {
            model.addAttribute("error", "Sender or recipient not found");
            model.addAttribute("users", userRepository.findAll());
            return "send-message";
        }

        messageService.sendMessage(sender.getId(), recipient.getId(), content);
        return "redirect:/messages/send";
    }

    @GetMapping("/received")
    public String getReceivedMessages(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String recipientUsername = authentication.getName();

        User recipient = userRepository.getUserByUsername(recipientUsername);
        if (recipient == null) {
            model.addAttribute("error", "Recipient not found");
            return "error"; // You can create an error page or handle this case as needed
        }

        List<Message> messages = messageService.getMessagesByRecipientId(recipient.getId());
        model.addAttribute("messages", messages);
        return "received-messages";
    }
}





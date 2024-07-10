package com.example.demo.Controllers;
import com.example.demo.Services.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestingEmail {

    @Autowired
    private MailSenderService emailService;

    @GetMapping("/send-email")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text
    ) {
        emailService.send(to, subject, text);
        return "Email sent successfully";
    }
}


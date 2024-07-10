package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
    @Autowired
    private  JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String username;
    public void send(String emailTo, String subject,String message){
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom(username);
        simpleMessage.setTo(emailTo);
        simpleMessage.setSubject(subject);
        simpleMessage.setText(message);

        mailSender.send(simpleMessage);
    }
}

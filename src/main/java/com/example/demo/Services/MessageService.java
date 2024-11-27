package com.example.demo.Services;

import com.example.demo.MongoDB.Message;
import com.example.demo.MySQL.User;
import com.example.demo.repo.MessageRepository;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public Message sendMessage(Integer senderId, Integer recipientId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setRecipientId(recipientId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        String username = userRepository.findById(message.getSenderId()).orElse(null).getUsername();
        message.setSenderName(username);
        return messageRepository.save(message);
    }

    public List<Message> getMessagesBySenderId(Integer senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    public List<Message> getMessagesByRecipientId(Integer recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

//    public List<User> getUsersBySenderId(List<Message> messages) {
//        List<User> users = new ArrayList<>();
//        for (Message message : messages) {
//            userRepository.findById(message.getSenderId()).ifPresent(users::add);
//        }
//        return users;
//    }
}


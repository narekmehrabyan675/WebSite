package com.example.demo.repo;

import com.example.demo.MongoDB.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderId(Integer senderId);
    List<Message> findByRecipientId(Integer recipientId);
}

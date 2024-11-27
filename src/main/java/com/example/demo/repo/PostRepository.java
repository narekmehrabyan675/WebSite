package com.example.demo.repo;

import com.example.demo.MongoDB.Like;
import com.example.demo.MongoDB.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
}




package com.example.demo.Services;

import com.example.demo.MongoDB.Like;
import com.example.demo.MongoDB.Post;
import com.example.demo.repo.LikeRepository;
import com.example.demo.repo.PostRepository;
import com.example.demo.MySQL.User;
import com.example.demo.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(String content, MultipartFile image, String username) {
        String imageUrl = saveImage(image);
        User user = userRepository.getUserByUsername(username);

        Post post = new Post();
        post.setContent(content);
        post.setCreatedAt(LocalDateTime.now());
        post.setImageUrl(imageUrl);
        post.setUserId(user.getId());

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            List<Like> likes = likeRepository.findByPostId(post.getId());
            post.setLikes(likes);
        }
        return posts;
    }

    public void likePost(String postId, String username) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Optional<Like> existingLike = likeRepository.findByPostIdAndUsername(postId, username);
            if (existingLike.isEmpty()) {
                Like like = new Like();
                like.setPost(post);
                like.setUsername(username);
                likeRepository.save(like);
            }
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    public Long countLikes(String postId) {
        return likeRepository.countByPostId(postId);
    }

    private String saveImage(MultipartFile image) {
        try {
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.copy(image.getInputStream(), path);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error saving image", e);
        }
    }
}

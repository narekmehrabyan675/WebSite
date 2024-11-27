package com.example.demo.Services;

import com.example.demo.MongoDB.Like;
import com.example.demo.MongoDB.Post;
import com.example.demo.repo.LikeRepository;
import com.example.demo.repo.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
    }

    public void likePost(String postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if (likeRepository.findByPostIdAndUsername(postId, username).isEmpty()) {
            Like like = new Like();
            like.setPost(post);
            like.setUsername(username);
            likeRepository.save(like);
        }
    }

    public Long countLikes(String postId) {
        return likeRepository.countByPostId(postId);
    }
}

package com.example.demo.Controllers;

import com.example.demo.Services.LikeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{postId}")
    public void likePost(@PathVariable String postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        likeService.likePost(postId, username);
    }

    @GetMapping("/{postId}/count")
    public Long countLikes(@PathVariable String postId) {
        return likeService.countLikes(postId);
    }
}

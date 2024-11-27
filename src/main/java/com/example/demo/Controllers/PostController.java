package com.example.demo.Controllers;

import com.example.demo.MongoDB.Post;
import com.example.demo.Services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public String createPost(@RequestParam String content, @RequestParam("image") MultipartFile image) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        postService.createPost(content, image, username);
        return "redirect:/posts/view";
    }

    @GetMapping
    @ResponseBody
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/view")
    public String viewPosts(Model model) {
        model.addAttribute("posts", postService.getAllPosts());
        return "posts";
    }

    @PostMapping("/likes/{postId}")
    public String likePost(@PathVariable String postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        postService.likePost(postId, username);
        return "redirect:/posts/view";
    }
}

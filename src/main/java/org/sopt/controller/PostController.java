package org.sopt.controller;

import org.sopt.domain.Post;
import org.sopt.service.PostService;

import java.util.List;
import java.util.Objects;

public class PostController {

    private final PostService postService = new PostService();

    public void createPost(final String title) {
        try {
            postService.createPost(title);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    public Post getPostById(int id) {
        return postService.getPostById(id);
    }


    public boolean deletePostById(int id) {
        return postService.deletePostById(id);
    }

    public List<Post> searchPostsByKeyword(String keyword) {
        return null;
    }

    public Boolean updatePostTitle(int id, String newTitle) {
        try {
            return postService.updatePostTitle(id, newTitle);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

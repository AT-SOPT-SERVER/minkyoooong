package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;

import java.util.List;

public class PostService {

    private final PostRepository postRepository = new PostRepository();
    private int postId = 1;

    public void createPost(String title) {
        Post post = new Post(postId++, title);

        postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(int id) {
        return postRepository.findPostById(id);
    }

    public boolean deletePostById(int id) {
        return postRepository.delete(id);
    }

    // 게시물 수정 -> service에서 처리.
    // 게시물 수정은 단순히 수정 이외에 게시물을 조회하고, 예외 등등을 처리해야하므로 비즈니스 로직을 수행하는 service에서 처리.
    public boolean updatePostTitle(int id, String newTitle) {
        Post post = postRepository.findPostById(id);

        if (post == null) {
            return false;
        }

        post.updateTitle(newTitle);
        return true;
    }

}

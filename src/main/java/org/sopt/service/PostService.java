package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.dto.PostResponse;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final long POST_LIMIT_SECONDS = 180; // 3분 제한

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse createPost(String title) {
        if (postRepository.existsByTitle(title)) {
            throw new CustomException(ErrorCode.DUPLICATE_TITLE);
        }

        List<Post> posts = postRepository.findAll();
        if (!posts.isEmpty()) {
            Post lastPost = posts.get(posts.size() - 1);
            Duration duration = Duration.between(lastPost.getCreatedAt(), LocalDateTime.now());
            if (duration.getSeconds() < POST_LIMIT_SECONDS) {
                throw new CustomException(ErrorCode.POST_COOLDOWN);
            }
        }

        // 생성 완료 후 응답에서 생성된 post 정보를 돌려보내도록 하기 위해 response 리턴하도록
        Post savedPost = postRepository.save(new Post(title));
        return new PostResponse(savedPost.getId(), savedPost.getTitle());
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle()))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return new PostResponse(post.getId(), post.getTitle());
    }

    public void deletePostById(Long id) {
        if (!postRepository.existsById(id)) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        postRepository.deleteById(id);
    }

    public void updatePostTitle(Long id, String newTitle) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (postRepository.existsByTitle(newTitle) && !post.getTitle().equals(newTitle)) {
            throw new CustomException(ErrorCode.DUPLICATE_TITLE);
        }

        post.updateTitle(newTitle);
        postRepository.save(post);
    }

    public List<PostResponse> searchPostsByKeyword(String keyword) {
        return postRepository.findByTitleContaining(keyword).stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle()))
                .collect(Collectors.toList());
    }
}

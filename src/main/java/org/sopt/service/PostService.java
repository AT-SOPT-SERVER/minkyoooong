package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.repository.PostRepository;
import org.sopt.utils.IdGenerator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    private final PostRepository postRepository = new PostRepository();

    public void createPost(String title) {

        // 중복 검사 로직 추가
        if (postRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("이미 존재하는 제목입니다.");
        }

        // 게시물 작성 제한 시간 3분 -> service에 구현.
        Post lastPost = postRepository.findLastPost();

        if (lastPost != null) {
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(lastPost.getCreatedAt(), now);
            if (duration.getSeconds() < 180) { // 제한 시간 3분
                throw new IllegalArgumentException("직전 게시물 생성 기준으로 3분이 지나지 않아 게시물을 작성할 수 없습니다.");
            }
        }

        // 유틸 클래스에서 id 값 받아오는 방식으로 수정
        int id = IdGenerator.generateId();
        Post post = new Post(id, title);
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

        // 현재 게시글을 제외하고 중복 제목 검사
        List<Post> allPosts = postRepository.findAll();
        for (Post p : allPosts) {
            if (p.getId() != id && p.getTitle().equals(newTitle)) {
                throw new IllegalArgumentException("이미 존재하는 제목입니다.");
            }
        }

        post.updateTitle(newTitle);
        return true;
    }

    public List<Post> searchPostsByKeyword(String keyword) {
        return postRepository.searchByKeyword(keyword);
    }


}

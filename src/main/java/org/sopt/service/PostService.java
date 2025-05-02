package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.TagType;
import org.sopt.domain.User;
import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponse;
import org.sopt.dto.PostSummaryResponse;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final long POST_LIMIT_SECONDS = 180; // 3분 제한

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public PostResponse createPost(PostRequest request, Long userId) {
        if (request.title() == null || request.title().isBlank())
            throw new CustomException(ErrorCode.INVALID_TITLE);
        if (request.content() == null || request.content().isBlank())
            throw new CustomException(ErrorCode.INVALID_CONTENT);

        if (request.title().length() > 30)
            throw new CustomException(ErrorCode.INVALID_TITLE);
        if (request.content().length() > 1000)
            throw new CustomException(ErrorCode.INVALID_CONTENT);

        if (postRepository.existsByTitle(request.title())) { // 제목 중복 검사 로직 서비스 계층에 추가
            throw new CustomException(ErrorCode.DUPLICATE_TITLE);
        }

        List<Post> posts = postRepository.findAll();
        List<Post> userPosts = postRepository.findAllByWriterIdOrderByCreatedAtDesc(userId);

        // user추가 -> 쿨타임을 user별 마지막 글 작성 시간 기준으로
        if (!userPosts.isEmpty()) {
            Post lastPost = userPosts.get(0);
            Duration duration = Duration.between(lastPost.getCreatedAt(), LocalDateTime.now());
            if (duration.getSeconds() < POST_LIMIT_SECONDS) {
                throw new CustomException(ErrorCode.POST_COOLDOWN);
            }
        }

        // 생성 완료 후 응답에서 생성된 post 정보를 돌려보내도록 하기 위해 response 리턴하도록
        User user = userService.findUserById(userId);
        Post saved = postRepository.save(new Post(request.title(), request.content(), request.tag(), user));
        return new PostResponse(saved.getId(), saved.getTitle(), saved.getContent(), user.getNickname(), saved.getTag());
    }


    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(post -> new PostSummaryResponse(post.getId(), post.getTitle(), post.getWriter().getNickname()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getWriter().getNickname(), post.getTag());
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getWriter().getId().equals(userId))
            throw new CustomException(ErrorCode.NO_PERMISSION);
        postRepository.delete(post);
    }

    @Transactional
    public PostResponse updatePost(Long postId, PostRequest request, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        if (!post.getWriter().getId().equals(userId))
            throw new CustomException(ErrorCode.NO_PERMISSION);

        // 수정 시 제목이 변경되는 경우 중복 여부 검사
        if (request.title() != null && !request.title().isBlank() &&
                !post.getTitle().equals(request.title())) {
            Optional<Post> found = postRepository.findByTitle(request.title());
            if (found.isPresent() && !found.get().getId().equals(post.getId())) {
                throw new CustomException(ErrorCode.DUPLICATE_TITLE);
            }
        }

        post.update(request.title(), request.content(), request.tag());
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getWriter().getNickname(), post.getTag());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchByTitle(String titleKeyword) {
        List<Post> posts = postRepository.findPostsByTitleContaining(titleKeyword);
        if (posts.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return posts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getWriter().getNickname(), p.getTag()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchByWriterNickname(String nicknameKeyword) {
        List<Post> posts = postRepository.findPostsByWriterNicknameContaining(nicknameKeyword);
        if (posts.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return posts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getWriter().getNickname(), p.getTag()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> searchByTag(String tagValue) {
        TagType tag;
        try {
            tag = TagType.valueOf(tagValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TAG);
        }

        List<Post> posts = postRepository.findByTag(tag);
        if (posts.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return posts.stream()
                .map(p -> new PostResponse(p.getId(), p.getTitle(), p.getContent(), p.getWriter().getNickname(), p.getTag()))
                .collect(Collectors.toList());
    }
}

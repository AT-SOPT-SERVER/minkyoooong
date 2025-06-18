package org.sopt.service;

import org.sopt.domain.Post;
import org.sopt.domain.TagType;
import org.sopt.domain.User;
import org.sopt.dto.CommentResponse;
import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponse;
import org.sopt.dto.PostSummaryResponse;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.global.PageResponse;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserService userService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public PostResponse createPost(PostRequest request, Long userId) {

        if (postRepository.existsByTitle(request.title())) {
            throw new CustomException(ErrorCode.DUPLICATE_TITLE);
        }

        List<Post> userPosts = postRepository.findAllByWriterIdOrderByCreatedAtDesc(userId);

        // user 추가 -> 쿨타임을 user별 마지막 글 작성 시간 기준으로
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
        return new PostResponse(saved.getId(), saved.getTitle(), saved.getContent(), user.getNickname(), saved.getTag(), List.of()) ;
    }


    @Transactional(readOnly = true)
    public PageResponse<List<PostSummaryResponse>> getAllPosts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> postPage = postRepository.findAll(pageable);

        List<PostSummaryResponse> result = postPage.getContent().stream()
                .map(post -> new PostSummaryResponse(post.getId(), post.getTitle(), post.getWriter().getNickname()))
                .collect(Collectors.toList());

        return new PageResponse<>(
                postPage.getNumber(),
                postPage.hasNext(),
                postPage.getTotalPages(),
                result
        );
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        List<CommentResponse> comments = commentRepository.findByPost(post).stream()
                .map(c -> new CommentResponse(c.getId(), c.getContent(), c.getWriter().getNickname()))
                .collect(Collectors.toList());

        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getWriter().getNickname(), post.getTag(), comments);
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

        // 제목이 변경된 경우에만 중복 검사
        if (!post.getTitle().equals(request.title())) {
            Optional<Post> found = postRepository.findByTitle(request.title());
            if (found.isPresent() && !found.get().getId().equals(post.getId())) {
                throw new CustomException(ErrorCode.DUPLICATE_TITLE);
            }
        }

        post.update(request.title(), request.content(), request.tag());
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getWriter().getNickname(), post.getTag(), List.of());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> search(String title, String writer, String tagValue) {
        final TagType tag;
        if (tagValue != null) {
            try {
                tag = TagType.valueOf(tagValue.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new CustomException(ErrorCode.INVALID_TAG);
            }
        } else {
            tag = null;
        }

        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        return posts.stream()
                .filter(post -> {
                    if (title != null && !post.getTitle().contains(title)) return false;
                    if (writer != null && !post.getWriter().getNickname().contains(writer)) return false;
                    if (tag != null && !post.getTag().equals(tag)) return false;
                    return true;
                })
                .map(post -> new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getWriter().getNickname(), post.getTag(),List.of())) // 검색결과에서는 댓글 포함 x
                .collect(Collectors.toList());
    }
}

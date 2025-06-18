package org.sopt.service;

import org.sopt.domain.Comment;
import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.CommentRequest;
import org.sopt.dto.CommentResponse;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Transactional
    public CommentResponse create(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userService.findUserById(userId);

        Comment comment = new Comment(request.content(), user, post);
        Comment saved = commentRepository.save(comment);

        return new CommentResponse(saved.getId(), saved.getContent(), user.getNickname());
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findByPost(post).stream()
                .map(c -> new CommentResponse(c.getId(), c.getContent(), c.getWriter().getNickname()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse update(Long commentId, Long userId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getWriter().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION); // 작성자만 수정 가능
        }

        comment.update(request.content());
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getWriter().getNickname());
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getWriter().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION); // 작성자만 삭제 가능
        }

        commentRepository.delete(comment);
    }
}

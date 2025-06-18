package org.sopt.service;

import org.sopt.domain.*;
import org.sopt.global.CustomException;
import org.sopt.global.ErrorCode;
import org.sopt.repository.CommentRepository;
import org.sopt.repository.LikeRepository;
import org.sopt.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, CommentRepository commentRepository, UserService userService) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Transactional
    public boolean likePost(Long postId, Long userId) {
        User user = userService.findUserById(userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return likeRepository.findByUserAndPost(user, post) // 좋아요 유무에 따라 존재하면 delete, 없으면 save
                .map(like -> {
                    likeRepository.delete(like); return false;
                })
                .orElseGet(() -> {
                    likeRepository.save(Like.forPost(user, post)); return true;
                });
    }

    @Transactional
    public boolean likeComment(Long commentId, Long userId) {
        User user = userService.findUserById(userId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        return likeRepository.findByUserAndComment(user, comment)
                .map(like -> {
                    likeRepository.delete(like); return false;
                })
                .orElseGet(() -> {
                    likeRepository.save(Like.forComment(user, comment)); return true;
                });
    }

    @Transactional(readOnly = true)
    public int countPostLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return likeRepository.countByPost(post);
    }

    @Transactional(readOnly = true)
    public int countCommentLikes(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
        return likeRepository.countByComment(comment);
    }
}

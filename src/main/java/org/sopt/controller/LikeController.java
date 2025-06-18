package org.sopt.controller;

import org.sopt.global.ApiResponse;
import org.sopt.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // 게시물 좋아요
    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<Boolean>> togglePostLike(
            @PathVariable Long postId,
            @RequestHeader("userId") Long userId
    ) {
        boolean liked = likeService.likePost(postId, userId);
        String message = liked ? "게시글 좋아요 완료" : "게시글 좋아요 취소";
        return ResponseEntity.ok(ApiResponse.success(message, liked));
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse<Boolean>> toggleCommentLike(
            @PathVariable Long commentId,
            @RequestHeader("userId") Long userId
    ) {
        boolean liked = likeService.likeComment(commentId, userId);
        String message = liked ? "댓글 좋아요 완료" : "댓글 좋아요 취소";
        return ResponseEntity.ok(ApiResponse.success(message, liked));
    }

    // 게시글 좋아요 수 조회
    @GetMapping("/posts/{postId}/count")
    public ResponseEntity<ApiResponse<Integer>> countPostLikes(@PathVariable Long postId) {
        int count = likeService.countPostLikes(postId);
        return ResponseEntity.ok(ApiResponse.success("게시글 좋아요 수 조회", count));
    }

    // 댓글 좋아요 수 조회
    @GetMapping("/comments/{commentId}/count")
    public ResponseEntity<ApiResponse<Integer>> countCommentLikes(@PathVariable Long commentId) {
        int count = likeService.countCommentLikes(commentId);
        return ResponseEntity.ok(ApiResponse.success("댓글 좋아요 수 조회", count));
    }
}
package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.dto.request.CommentRequest;
import org.sopt.dto.response.CommentResponse;
import org.sopt.global.response.ApiResponse;
import org.sopt.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response = commentService.create(postId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("댓글 작성 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable Long postId) {

        List<CommentResponse> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success("댓글 조회 성공", comments));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse updated = commentService.update(commentId, userId, request);
        return ResponseEntity.ok(ApiResponse.success("댓글 수정 성공", updated));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("userId") Long userId) {

        commentService.delete(commentId, userId);
        return ResponseEntity.ok(ApiResponse.success("댓글 삭제 성공"));
    }
}

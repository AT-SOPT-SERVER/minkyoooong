package org.sopt.controller;

import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponse;
import org.sopt.global.ApiResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest request) {
        PostResponse response = postService.createPost(request.title());
        return ResponseEntity.ok(ApiResponse.success("게시글이 성공적으로 작성되었습니다.", response));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PostResponse>>> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.success("전체 게시글 조회 성공", postService.getAllPosts()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("게시글 조회 성공", postService.getPostById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long id, @RequestBody PostRequest request) {
        postService.updatePostTitle(id, request.title());
        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공"));
    }
}
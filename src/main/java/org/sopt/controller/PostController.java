package org.sopt.controller;

import org.sopt.dto.PostRequest;
import org.sopt.dto.PostResponse;
import org.sopt.dto.PostSummaryResponse;
import org.sopt.global.ApiResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post") // userid는 헤더에서 받는다.
    public ResponseEntity<ApiResponse<PostResponse>> createPost(@RequestBody PostRequest request,
                                                                @RequestHeader("userId") Long userId) {
        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 작성 성공", response));
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<List<PostSummaryResponse>>> getAllPosts() {
        List<PostSummaryResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(ApiResponse.success("전체 게시글 조회 성공", posts));
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("게시글 조회 성공", postService.getPostById(id)));
    }

    @PatchMapping("/post/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id,
                                                                @RequestBody PostRequest request,
                                                                @RequestHeader("userId") Long userId) {
        PostResponse updated = postService.updatePost(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", updated));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id,
                                                        @RequestHeader("userId") Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공"));
    }

    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchByTitle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(ApiResponse.success("제목 검색 성공", postService.searchByTitle(keyword)));
    }

    @GetMapping("/search/writer")
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchByWriter(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(ApiResponse.success("작성자 검색 성공", postService.searchByWriterNickname(nickname)));
    }

    @GetMapping("/search/tag")
    public ResponseEntity<ApiResponse<List<PostResponse>>> searchByTag(@RequestParam("tag") String tag) {
        return ResponseEntity.ok(ApiResponse.success("태그 검색 성공", postService.searchByTag(tag)));
    }
}
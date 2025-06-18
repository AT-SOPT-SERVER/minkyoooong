package org.sopt.controller;

import jakarta.validation.Valid;
import org.sopt.dto.request.PostRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.dto.response.PostSummaryResponse;
import org.sopt.global.response.ApiResponse;
import org.sopt.global.response.PageResponse;
import org.sopt.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @Valid @RequestBody PostRequest request,
            @RequestHeader("userId") Long userId) {
        PostResponse response = postService.createPost(request, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 작성 성공", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<PostSummaryResponse>>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size // 10개 단위로
    ) {
        PageResponse<List<PostSummaryResponse>> response = postService.getAllPosts(page, size);
        return ResponseEntity.ok(ApiResponse.success("전체 게시글 조회 성공", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("게시글 조회 성공", postService.getPostById(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(@PathVariable Long id,
                                                                @Valid @RequestBody PostRequest request,
                                                                @RequestHeader("userId") Long userId) {
        PostResponse updated = postService.updatePost(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 수정 성공", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id,
                                                        @RequestHeader("userId") Long userId) {
        postService.deletePost(id, userId);
        return ResponseEntity.ok(ApiResponse.success("게시글 삭제 성공"));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PostResponse>>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String writer,
            @RequestParam(required = false) String tag
    ) {
        if (title != null || writer != null || tag != null) {
            return ResponseEntity.ok(ApiResponse.success("검색 성공", postService.search(title, writer, tag)));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.fail("검색 조건을 하나 이상 입력해주세요."));
        }
    }

}
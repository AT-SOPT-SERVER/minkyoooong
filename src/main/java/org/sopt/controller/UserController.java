package org.sopt.controller;

import org.sopt.dto.UserRequest;
import org.sopt.dto.UserResponse;
import org.sopt.global.ApiResponse;
import org.sopt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@RequestBody UserRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", response));
    }
}
package org.sopt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "닉네임은 비어있을 수 없습니다.")
        @Size(max = 10, message = "닉네임은 10자를 넘을 수 없습니다.")
        String nickname
) {}

package org.sopt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
        @Size(max = 300, message = "댓글은 300자 이내여야 합니다.")
        String content
) {}

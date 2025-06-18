package org.sopt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.domain.TagType;

public record PostRequest(
        @NotBlank(message = "제목은 비어있을 수 없습니다.")
        @Size(max = 30, message = "제목은 30자 이하여야 합니다.")
        String title,

        @NotBlank(message = "내용은 비어있을 수 없습니다.")
        @Size(max = 1000, message = "내용은 1000자 이하여야 합니다.")
        String content,

        @NotNull(message = "태그는 필수입니다.")
        TagType tag
) {}
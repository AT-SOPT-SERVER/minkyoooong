package org.sopt.dto;

public record CommentResponse(
        Long id,
        String content,
        String writerNickname
) {}

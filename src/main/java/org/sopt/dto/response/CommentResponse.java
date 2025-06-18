package org.sopt.dto.response;

public record CommentResponse(
        Long id,
        String content,
        String writerNickname
) {}

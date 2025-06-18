package org.sopt.dto.response;

import org.sopt.domain.TagType;

import java.util.List;

public record PostResponse(Long id, String title, String content, String writerNickname, TagType tag, List<CommentResponse> comments) {}
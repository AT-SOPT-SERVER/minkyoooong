package org.sopt.dto;

import org.sopt.domain.TagType;

public record PostResponse(Long id, String title, String content, String writerNickname, TagType tag) {}
package org.sopt.dto;

import org.sopt.domain.TagType;

//dto는 데이터 전달 목적. record가 가장 적합 (불변, 생성자 + getter + equals + hashCode + toString 자동 생성)
public record PostRequest(String title, String content, TagType tag) {}
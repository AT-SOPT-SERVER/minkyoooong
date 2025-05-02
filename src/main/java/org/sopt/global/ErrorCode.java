package org.sopt.global;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,400, "해당 게시글을 찾을 수 없습니다."),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, 401, "이미 존재하는 제목입니다."),
    POST_COOLDOWN(HttpStatus.BAD_REQUEST,402, "마지막 게시글 작성 후 3분이 지나야 합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500, "서버 내부 오류입니다."),

    INVALID_TITLE(HttpStatus.BAD_REQUEST, 400, "제목은 비어있을 수 없으며 30자 이하여야 합니다."),
    INVALID_CONTENT(HttpStatus.BAD_REQUEST, 400, "내용은 비어있을 수 없으며 1000자 이하여야 합니다."),
    NO_PERMISSION(HttpStatus.FORBIDDEN, 403, "작성자만 수정 및 삭제가 가능합니다."),
    INVALID_TAG(HttpStatus.BAD_REQUEST,400, "유효하지 않은 태그입니다. 백엔드, 데이터베이스, 인프라 중 하나를 선택해주세요."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 403, "존재하지 않는 사용자입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, 409, "이미 존재하는 닉네임입니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, 400, "닉네임은 비어있을 수 없고 10자를 넘을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status,int code, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

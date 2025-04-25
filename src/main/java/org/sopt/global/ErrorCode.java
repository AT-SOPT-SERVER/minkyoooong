package org.sopt.global;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,400, "해당 게시글을 찾을 수 없습니다."),
    DUPLICATE_TITLE(HttpStatus.BAD_REQUEST, 401, "이미 존재하는 제목입니다."),
    POST_COOLDOWN(HttpStatus.BAD_REQUEST,402, "마지막 게시글 작성 후 3분이 지나야 합니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,500, "서버 내부 오류입니다.");

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

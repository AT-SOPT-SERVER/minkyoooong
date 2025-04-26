package org.sopt.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // CustomException이 던져졌을 때 여기로.
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        ErrorCode code = e.getErrorCode();
        return ResponseEntity.status(code.getStatus())
                .body(new ApiResponse<>(false, code.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception e) {
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ApiResponse<>(false, ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), null));
    }
}

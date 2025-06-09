package org.leisureup.global.exception;

import org.leisureup.global.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFound.class)
    public ApiResponse<?> notFound(NotFound e) {
        return ApiResponse.failure(e.getMessage());
    }
}

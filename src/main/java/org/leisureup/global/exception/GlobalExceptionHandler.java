package org.leisureup.global.exception;

import org.leisureup.global.response.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> customException(CustomException e) {
        int code = e.getCode();
        String message = e.getMessage();
        return ApiResponse.failure(code, message);
    }

}

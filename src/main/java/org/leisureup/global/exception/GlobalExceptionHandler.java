package org.leisureup.global.exception;

import java.util.*;
import org.leisureup.global.response.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> customException(CustomException e) {
        int code = e.getCode();
        String message = e.getMessage();
        return ApiResponse.failure(code, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> reqArgNotValidException(MethodArgumentNotValidException e) {

        List<String> errorMessages = e.getBindingResult().getFieldErrors().stream()
                .map(GlobalExceptionHandlerUtil::extractFieldErrorMsg)
                .toList();

        return ApiResponse.failure(400, "Bad Request", errorMessages);
    }
}

class GlobalExceptionHandlerUtil {

    static String extractFieldErrorMsg(FieldError fieldError) {
        return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
    }
}
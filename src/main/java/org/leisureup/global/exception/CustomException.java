package org.leisureup.global.exception;

import lombok.*;

@Getter
public class CustomException extends RuntimeException {

    private final int code;
    private final String message;

    protected CustomException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

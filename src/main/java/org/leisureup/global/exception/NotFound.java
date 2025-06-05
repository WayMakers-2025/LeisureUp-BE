package org.leisureup.global.exception;

public class NotFound extends RuntimeException {
    public NotFound(String explanation) {
        super(explanation);
    }
}

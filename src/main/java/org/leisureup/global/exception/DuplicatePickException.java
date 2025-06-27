package org.leisureup.global.exception;

public class DuplicatePickException extends CustomException {

    public DuplicatePickException(String message) {
        super(409, message);
    }
}

package org.leisureup.global.exception;

public class InvalidIdentityTokenException extends CustomException {

    public InvalidIdentityTokenException(int code, String message) {
        super(code, message);
    }
}

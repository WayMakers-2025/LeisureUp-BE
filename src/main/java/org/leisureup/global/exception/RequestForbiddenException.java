package org.leisureup.global.exception;

public class RequestForbiddenException extends CustomException {

    public RequestForbiddenException(String message) {
        super(403, message);
    }

}

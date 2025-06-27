package org.leisureup.global.exception;

public class NotFound extends CustomException {

    public NotFound(String explanation) {
        super(404, explanation);
    }
}

package org.leisureup.global.exception;

public class NotImplemented extends CustomException {

    private static final String defaultMessage = "Not implemented";

    public NotImplemented() {
        super(500, defaultMessage);
    }

    public NotImplemented(String message) {
        super(500, message);
    }
}

package org.leisureup.global.exception;

public class ClientSideException extends CustomException {

    public ClientSideException(int code, String message) {
        super(code, message);

        if (400 > code || code >= 500) {
            throw new IllegalArgumentException("Client side exception must be 400 - 500");
        }
    }
}

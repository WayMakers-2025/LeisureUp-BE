package org.leisureup.global.exception;

public class ServerSideException extends CustomException {

    public ServerSideException(int code, String message) {
        super(code, message);

        if (500 > code || code >= 600) {
            throw new IllegalArgumentException("Server side exception must be 500 - 600");
        }
    }
}

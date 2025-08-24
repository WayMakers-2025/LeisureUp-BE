package org.leisureup.global.auth.token.internal;

import org.leisureup.global.exception.*;

public class InvalidTokenException extends CustomException {

    public InvalidTokenException(int code) {
        super(code, "Invalid token");
    }
}

package org.leisureup.global.auth.aspect;

import org.leisureup.global.exception.*;

public class UnauthenticatedException extends CustomException {

    public UnauthenticatedException() {
        super(401, "Unauthenticated");
    }
}

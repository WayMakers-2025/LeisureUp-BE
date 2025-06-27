package org.leisureup.global.auth.social.internal;

import feign.*;
import feign.codec.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;

@Slf4j
public abstract class DefaultOAuthApiErrorDecoder implements ErrorDecoder {

    private final String messagePrefix;

    protected DefaultOAuthApiErrorDecoder(String messagePrefix) {
        this.messagePrefix = messagePrefix.trim();
    }

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        String reason = response.reason();

        String msg = String.format(
                "%s -- [%d:%s]", messagePrefix, status, reason
        );

        CustomException ex;
        if (400 <= status && status < 500) {
            ex = new ClientSideException(status, msg);
        } else if (500 <= status && status < 600) {
            ex = new ServerSideException(status, msg);
        } else {
            msg = String.format("Unexpected response status %d on %s", status, methodKey);
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        return ex;
    }
}

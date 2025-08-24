package org.leisureup.global.response.external;

import feign.*;
import feign.codec.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;

@Slf4j
public class DefaultFeignErrorDecoder implements ErrorDecoder {

    private final String messagePrefix;
    private static final String DEFAULT_MSG_PREFIX
            = "외부 API 통신 중 문제가 발생했습니다.";

    public DefaultFeignErrorDecoder() {
        messagePrefix = DEFAULT_MSG_PREFIX;
    }

    public DefaultFeignErrorDecoder(String messagePrefix) {
        this.messagePrefix = messagePrefix;
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

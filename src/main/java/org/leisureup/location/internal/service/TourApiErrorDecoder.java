package org.leisureup.location.internal.service;

import feign.*;
import feign.codec.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;

@Slf4j
@SuppressWarnings("DuplicatedCode")
public class TourApiErrorDecoder implements ErrorDecoder {

    private static final String MSG_PREFIX = """
            TourApi 요청에 문제가 발생했습니다.
            """.trim();

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        String reason = response.reason();

        String msg = String.format(
                "%s -- [%d:%s]", MSG_PREFIX, status, reason
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

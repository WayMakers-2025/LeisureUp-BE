package org.leisureup.global.config;

import lombok.extern.slf4j.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.task.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class RequestAndTraceIdDecorator implements TaskDecorator {

    private final String ridKeyOnMdc;
    private final String tidKeyOnMdc;

    public RequestAndTraceIdDecorator(
            @Value("${mdc-key.request-id}")
            String ridKeyOnMdc,
            @Value("${mdc-key.trace-id}")
            String tidKeyOnMdc
    ) {
        this.ridKeyOnMdc = ridKeyOnMdc;
        this.tidKeyOnMdc = tidKeyOnMdc;
    }

    @Override
    public @NonNull Runnable decorate(@NonNull Runnable runnable) {

        log.debug("Initializing trace ID");
        String rid = MDC.get(ridKeyOnMdc);
        String tid = RequestIdConfigurer.genRandomId();
        log.debug(
                "New trace ID has been generated : [{}] --> [{}]",
                rid, tid
        );

        return () -> {
            try {
                log.debug("Setting trace ID");
                MDC.put(ridKeyOnMdc, rid);
                MDC.put(tidKeyOnMdc, tid);
                log.debug("Trace ID has been set");
                runnable.run();
            } finally {
                log.debug("Destroying trace ID");
                MDC.remove(ridKeyOnMdc);
                MDC.remove(tidKeyOnMdc);
                log.debug("Trace ID has been destroyed");
            }
        };
    }
}

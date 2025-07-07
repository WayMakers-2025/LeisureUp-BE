package org.leisureup.global.config;

import jakarta.annotation.*;
import jakarta.servlet.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class RequestIdHandler implements ServletRequestListener {

    private static final int ID_LENGTH = 8;
    private final String ridKeyOnMdc;

    public RequestIdHandler(
            @Value("${mdc-key.request-id}")
            String ridKeyOnMdc
    ) {
        this.ridKeyOnMdc = ridKeyOnMdc;
    }

    private static String genRandomId() {
        try {
            String uuid = UUID.randomUUID().toString()
                    .replaceAll("-", "")
                    .substring(0, ID_LENGTH);

            return String.format(
                    "%s-%s",
                    uuid.substring(0, ID_LENGTH / 2),
                    uuid.substring(ID_LENGTH / 2)
            );
        } catch (Exception e) {
            log.error("Failed to generate random ID", e);
            return "UNKNOWN_ID";
        }
    }

    @PostConstruct
    public void validate() {
        if (ID_LENGTH <= 0) {
            throw new IllegalStateException("ID must be greater than zero.");
        }

        if (ridKeyOnMdc == null || ridKeyOnMdc.trim().isEmpty()) {
            throw new IllegalStateException("MDC RID key must not be empty.");
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        log.debug("Initializing request ID");
        MDC.put(ridKeyOnMdc, genRandomId());
        log.debug("Request ID has been initialized.");
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        log.debug("Destroying request ID");
        MDC.remove(ridKeyOnMdc);
        log.debug("Request ID has been destroyed.");
    }
}

package org.leisureup.global.response.external;

import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.codec.*;
import java.util.concurrent.*;
import org.springframework.context.annotation.*;

public class DefaultXmlRetryingConfig {

    private static final long PERIOD = 1000L;
    private static final long MAX_PERIOD = 2000L;
    private static final int MAX_ATTEMPTS = 3;

    private static final long CONNECT_TIMEOUT_MS = 1000L;
    private static final long READ_TIMEOUT_MS = 2000L;

    private static final long RETRY_DELAY_MS = 50L;

    @Bean
    public Decoder xmlRetyringDecoder(ObjectMapper objMapper) {
        return new DefaultXmlRetryingDecoder(objMapper, RETRY_DELAY_MS);
    }

    @Bean
    public ErrorDecoder xmlRetyringErrorDecoder() {
        return new DefaultFeignErrorDecoder();
    }

    @Bean
    public Retryer xmlRetryer() {
        return new Retryer.Default(
                PERIOD, MAX_PERIOD, MAX_ATTEMPTS
        );
    }

    @Bean
    public Request.Options xmlRetyringRequestOptions() {
        return new Request.Options(
                CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS,
                READ_TIMEOUT_MS, TimeUnit.MILLISECONDS,
                false
        );
    }
}

package org.leisureup.global.response.external;

import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.Request.HttpMethod;
import feign.codec.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.util.*;

@Slf4j
@RequiredArgsConstructor
public class DefaultXmlRetryingDecoder implements Decoder {

    private final ObjectMapper objMapper;
    private final long retryDelayMs;

    @Override
    public Object decode(Response response, Type type)
            throws IOException, FeignException {

        String contentType = response.headers()
                .get(HttpHeaders.CONTENT_TYPE).stream()
                .findFirst().orElse("");

        if (
                contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
                contentType.contains(MediaType.TEXT_XML_VALUE)
        ) {
            log.warn("XML content received, throwing RetryableException.");
            throw this.getRetryableException(
                    response,
                    "XML format is not supported"
            );
        }

        if (!contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            log.error(
                    "Expected Json exists on acceptable content type in [{}]",
                    contentType
            );
            throw this.getRetryableException(
                    response,
                    "Expected Json formatted response, no acceptable content type found."
            );
        }

        String body = StreamUtils.copyToString(
                response.body().asInputStream(), Charset.defaultCharset()
        );

        return objMapper.readValue(body, objMapper.constructType(type));
    }

    private RetryableException getRetryableException(
            Response response, String message
    ) {
        int status = response.status();
        HttpMethod httpMethod = response.request().httpMethod();
        Request request = response.request();

        return new RetryableException(
                status, message, httpMethod, null,
                retryDelayMs, request
        );
    }
}

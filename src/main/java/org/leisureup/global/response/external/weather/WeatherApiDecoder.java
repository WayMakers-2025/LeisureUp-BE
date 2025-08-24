package org.leisureup.global.response.external.weather;

import com.fasterxml.jackson.databind.*;
import feign.*;
import feign.codec.*;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.util.*;

@Slf4j
public class WeatherApiDecoder implements Decoder {

    private final ObjectMapper objMapper;

    public WeatherApiDecoder(ObjectMapper objMapper) {
        this.objMapper = objMapper;
    }

    private static RetryableException getRetryableException(
            Response response, String message
    ) {
        return new RetryableException(
                response.status(),
                message,
                response.request().httpMethod(),
                null, 30L,
                response.request()
        );
    }

    @Override
    public Object decode(Response response, Type type)
            throws IOException, DecodeException, FeignException {

        String contentType = response.headers().getOrDefault(HttpHeaders.CONTENT_TYPE, Set.of())
                .stream().findFirst().orElse("");

        String body = StreamUtils.copyToString(response.body().asInputStream(),
                Charset.defaultCharset());

        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return objMapper.readValue(body, objMapper.constructType(type));
        } else if (
                contentType.contains(MediaType.APPLICATION_XML_VALUE) ||
                contentType.contains(MediaType.TEXT_XML_VALUE)
        ) {
            log.info("hi!");
            throw getRetryableException(response, "XML 에러 응답을 받았습니다.");
        } else {
            throw new DecodeException(
                    response.status(),
                    "Unsupported response format: " + contentType,
                    response.request()
            );
        }
    }
}

package org.leisureup.global.response;

import lombok.extern.slf4j.*;
import org.springframework.core.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.http.server.*;
import org.springframework.lang.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@Slf4j
@RestControllerAdvice(basePackages = "org.leisureup")
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            @NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body, @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response
    ) {

        if (body instanceof ApiResponse<?> api) {
            int code = api.getCode();
            HttpStatusCode httpStatusCode = HttpStatus.valueOf(code);
            response.setStatusCode(httpStatusCode);
        } else {
            log.warn(
                    "Incompatible response type encountered: {}",
                    body.getClass().getSimpleName()
            );
        }

        return body;
    }
}

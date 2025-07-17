package org.leisureup.global.response;

import java.util.*;
import lombok.extern.slf4j.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.web.error.*;
import org.springframework.boot.web.servlet.error.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;

@Slf4j
@Component
public class CustomErrorAttribute extends DefaultErrorAttributes {

    private final String ridKeyOnMdc;

    public CustomErrorAttribute(
            @Value("${mdc-key.request-id}")
            String ridKeyOnMdc
    ) {
        this.ridKeyOnMdc = ridKeyOnMdc;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest,
            ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        String requestId = MDC.get(ridKeyOnMdc);
        errorAttributes.put("requestId", requestId);

        return errorAttributes;
    }
}

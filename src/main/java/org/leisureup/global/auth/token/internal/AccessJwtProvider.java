package org.leisureup.global.auth.token.internal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * Access token 발급, 파싱을 위한 component
 */
@Component
public class AccessJwtProvider extends AbstractJwtProvider {

    public AccessJwtProvider(
            @Value("${secret.jwt.access-token.key}")
            String key,
            @Value("${secret.jwt.access-token.expiration}")
            long expiration
    ) {
        super(key, expiration);
    }
}

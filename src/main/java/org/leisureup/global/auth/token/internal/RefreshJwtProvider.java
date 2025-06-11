package org.leisureup.global.auth.token.internal;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

/**
 * Refresh token 발급, 파싱을 위한 component
 */
@Component
public class RefreshJwtProvider extends AbstractJwtProvider {

    protected RefreshJwtProvider(
            @Value("${secret.jwt.refresh-token.key}")
            String key,
            @Value("${secret.jwt.refresh-token.expiration}")
            long expiration
    ) {
        super(key, expiration);
    }
}

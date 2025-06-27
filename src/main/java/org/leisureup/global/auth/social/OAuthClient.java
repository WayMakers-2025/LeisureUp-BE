package org.leisureup.global.auth.social;

import org.leisureup.global.auth.dto.request.SignInUpRequest.*;

public interface OAuthClient {

    /**
     * OAuth2 로 사용자 정보를 가져온다.
     */
    OAuthResponse fetchInfo(String token);

    AuthType getType();
}

package org.leisureup.global.auth.social;

import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.springframework.stereotype.*;

@Component
public class KakaoOAuthClient implements OAuthClient {

    @Override
    public OAuthResponse fetchInfo(String token) {
        // TODO : Kakao OAuth 구현
        return null;
    }

    @Override
    public AuthType getType() {
        return AuthType.KAKAO;
    }
}

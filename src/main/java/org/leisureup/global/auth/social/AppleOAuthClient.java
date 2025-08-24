package org.leisureup.global.auth.social;

import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.exception.*;
import org.springframework.stereotype.*;

@Component
public class AppleOAuthClient implements OAuthClient {

    @Override
    public OAuthResponse fetchInfo(String token) {
        // TODO : Apple OAuth 구현
        throw new NotImplemented("Apple OAuth Client not implemented yet");
    }

    @Override
    public AuthType getType() {
        return AuthType.APPLE;
    }
}

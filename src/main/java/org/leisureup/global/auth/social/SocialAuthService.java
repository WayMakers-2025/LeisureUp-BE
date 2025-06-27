package org.leisureup.global.auth.social;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.auth.dto.*;
import org.leisureup.global.auth.dto.request.*;
import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.auth.dto.response.*;
import org.leisureup.global.auth.token.service.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
public class SocialAuthService {

    private final MemberSpi memberSpi;
    private final Map<AuthType, OAuthClient> oauthClients;
    private final TokenAuthService tokenAuthService;

    public SocialAuthService(
            MemberSpi memberSpi, List<OAuthClient> oauthClients,
            TokenAuthService tokenAuthService
    ) {
        this.memberSpi = memberSpi;
        this.oauthClients = oauthClients.stream().collect(
                Collectors.toMap(OAuthClient::getType, Function.identity())
        );
        this.tokenAuthService = tokenAuthService;
    }

    private static SocialType getType(AuthType authType) {
        return switch (authType) {
            case APPLE -> SocialType.APPLE;
            case GOOGLE -> SocialType.GOOGLE;
            case KAKAO -> SocialType.KAKAO;
        };
    }

    /**
     * 소셜 인증 token 을 통해 로그인하거나 회원가입한다.
     */
    public SignInUpResponse signInOrSignUp(SignInUpRequest req) {

        // OAuth 로 사용자 정보를 가져온다.
        AuthType authType = req.authType();
        OAuthResponse oauthResponse = oauthClients.get(authType)
                .fetchInfo(req.token());
        String socialId = oauthResponse.socialId();

        // 사용자가 가입되어 있는지 아닌지 확인한다.
        SocialType socialType = getType(authType);
        Long memberId = memberSpi.getMemberIdWithSocial(socialType, socialId)
                .orElse(null);

        Tokens tokens;

        if (memberId != null) {     // 가입되어 있으면 토큰을 생성한다.
            tokens = tokenAuthService.createTokens(memberId);
            memberId = null;
        } else {                    // 가입되어 있지 않으면 사용자를 생성하고 토큰을 생성한다.
            memberId = memberSpi.saveNewMember(
                    socialType, socialId,
                    oauthResponse.name(), oauthResponse.email()
            );
            tokens = tokenAuthService.createTokens(memberId);
        }

        return SignInUpResponse.of(memberId, tokens.accessToken(), tokens.refreshToken());
    }

}

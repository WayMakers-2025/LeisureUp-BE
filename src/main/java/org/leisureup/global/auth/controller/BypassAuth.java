package org.leisureup.global.auth.controller;

import lombok.*;
import org.leisureup.global.auth.dto.*;
import org.leisureup.global.auth.dto.response.*;
import org.leisureup.global.auth.token.service.*;
import org.leisureup.member.spi.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
@Profile("local")
@RequiredArgsConstructor
public class BypassAuth {

    private final MemberSpi memberSpi;
    private final TokenAuthService tokenAuthService;

    public SignInUpResponse createNewMember(
            SocialType type, String socialId,
            String nickname, String email
    ) {
        Long id = memberSpi.saveNewMember(type, socialId, nickname, email);
        Tokens t = tokenAuthService.createTokens(id);

        return SignInUpResponse.of(id, t.accessToken(), t.refreshToken());
    }
}

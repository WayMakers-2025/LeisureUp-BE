package org.leisureup.global.auth.controller;

import org.leisureup.global.auth.dto.response.*;
import org.leisureup.global.auth.token.service.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.spi.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;

@Component
@Profile("!local")
public class BypassAuthOnRemote extends BypassAuth {

    public BypassAuthOnRemote(
            MemberSpi memberSpi,
            TokenAuthService tokenAuthService
    ) {
        super(memberSpi, tokenAuthService);
    }

    @Override
    public SignInUpResponse createNewMember(
            SocialType type, String socialId,
            String nickname, String email
    ) {
        throw new BadRequestException("해당 서비스는 local profile 에서만 가능합니다.");
    }
}

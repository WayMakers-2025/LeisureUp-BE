package org.leisureup.global.auth.controller;

import jakarta.validation.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.auth.dto.*;
import org.leisureup.global.auth.dto.request.*;
import org.leisureup.global.auth.dto.response.*;
import org.leisureup.global.auth.social.*;
import org.leisureup.global.auth.token.service.*;
import org.leisureup.global.response.*;
import org.leisureup.member.spi.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SocialAuthService socialAuthService;
    private final TokenAuthService tokenAuthService;
    private final BypassAuth bypassAuth;


    @PostMapping            // 소셜 로그인 or 회원가입
    public ApiResponse<SignInUpResponse> loginSingUp(
            @Valid @RequestBody SignInUpRequest req
    ) {
        var resp = socialAuthService.signInOrSignUp(req);
        boolean created = resp.id() != null;
        return ApiResponse.success(
                created ? 201 : 200,
                resp
        );
    }


    @PostMapping("/token")  // 토큰 재발급
    public ApiResponse<RecreateTokenResponse> recreateToken(
            @Valid @RequestBody RecreateTokenRequest req
    ) {
        String rt = req.refreshToken();
        Long memberId = tokenAuthService.getMemberIdFromRt(rt);
        Tokens tokens = tokenAuthService.createTokens(memberId);

        return ApiResponse.success(
                200, RecreateTokenResponse.of(tokens)
        );
    }

    @PostMapping("/new")
    public ApiResponse<SignInUpResponse> createNewMember(
            @RequestParam SocialType type, @RequestParam String socialId,
            @RequestParam String nickname, @RequestParam String email
    ) {
        var resp = bypassAuth.createNewMember(type, socialId, nickname, email);

        return ApiResponse.success(201, resp);
    }
}

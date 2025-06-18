package org.leisureup.global.auth.social;

import lombok.*;
import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.auth.social.internal.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "KakaoApi",
        url = "https://kapi.kakao.com",
        configuration = KakaoApiErrorDecoder.class
)
interface KakaoApi {

    @GetMapping("/v1/oidc/userinfo")
    GetUserInfoKakao getUserInfo(
            @RequestHeader("Authorization") String bearerToken
    );

}

@Component
@RequiredArgsConstructor
public class KakaoOAuthClient implements OAuthClient {

    private final KakaoApi kakaoApi;

    @Override
    public OAuthResponse fetchInfo(String token) {

        String bearerToken = String.format("Bearer %s", token);
        GetUserInfoKakao userInfo = kakaoApi.getUserInfo(bearerToken);

        return userInfo.toOAuthResponse();
    }

    @Override
    public AuthType getType() {
        return AuthType.KAKAO;
    }
}

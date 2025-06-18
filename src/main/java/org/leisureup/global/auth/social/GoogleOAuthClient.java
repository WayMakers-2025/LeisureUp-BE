package org.leisureup.global.auth.social;

import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.auth.social.internal.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "GoogleApi",
        url = "https://openidconnect.googleapis.com",
        configuration = GoogleApiErrorDecoder.class
)
interface GoogleApi {

    @GetMapping("/v1/userinfo")
    GetUserInfoGoogle getUserInfo(
            @RequestHeader("Authorization") String bearerToken
    );

}

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuthClient implements OAuthClient {

    private final GoogleApi googleApi;

    @Override
    public OAuthResponse fetchInfo(String token) {

        String bearerToken = String.format("Bearer %s", token);
        GetUserInfoGoogle userInfo = googleApi.getUserInfo(bearerToken);

        return userInfo.toOAuthResponse();
    }

    @Override
    public AuthType getType() {
        return AuthType.GOOGLE;
    }
}

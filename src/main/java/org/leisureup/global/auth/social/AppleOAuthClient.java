package org.leisureup.global.auth.social;

import lombok.*;
import org.leisureup.global.auth.dto.api.*;
import org.leisureup.global.auth.dto.api.GetAppleOidcOpenKeys.*;
import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.auth.social.internal.*;
import org.leisureup.global.exception.*;
import org.springframework.cache.annotation.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "AppleOidcClient",
        url = "${feign.openid.apple.oidc-key}",
        configuration = AppleApiErrorDecoder.class
)
interface AppleOidcClient {

    @GetMapping("/auth/keys")
    @Cacheable(cacheNames = "apple-oidc-open-keys")
    GetAppleOidcOpenKeys getAppleOidcOpenKeys();
}

@Component
@RequiredArgsConstructor
public class AppleOAuthClient implements OAuthClient {

    private final AppleOidcClient appleOidcClient;
    private final AppleOidcHelper appleOidcHelper;

    @Override
    public OAuthResponse fetchInfo(String idToken) {

        GetAppleOidcOpenKeys publicKeys = appleOidcClient.getAppleOidcOpenKeys();
        String kid = appleOidcHelper.getKidClaimUnsignedFrom(idToken);

        AppleOidcKey matchingKey = publicKeys.keys().stream()
                .filter(k -> k.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new InvalidIdentityTokenException(
                        401, "No matching key found with given token."
                ));

        return appleOidcHelper.getVerifiedInfoFrom(idToken, matchingKey.n(), matchingKey.e());
    }

    @Override
    public AuthType getType() {
        return AuthType.APPLE;
    }
}


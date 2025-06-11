package org.leisureup.global.auth.social;

public record OAuthResponse(
        Long socialId,
        String name,
        String email
) {

    public static OAuthResponse of(Long socialId, String name, String email) {
        return new OAuthResponse(socialId, name, email);
    }
}

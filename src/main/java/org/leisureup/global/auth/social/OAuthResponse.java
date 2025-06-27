package org.leisureup.global.auth.social;

public record OAuthResponse(
        String socialId,
        String name,
        String email
) {

    public static OAuthResponse of(String socialId, String name, String email) {
        return new OAuthResponse(socialId, name, email);
    }
}

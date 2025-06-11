package org.leisureup.global.auth.dto.response;

public record SignInUpResponse(
        Long id,
        String accessToken,
        String refreshToken
) {


    public static SignInUpResponse of(
            Long id, String accessToken, String refreshToken
    ) {
        return new SignInUpResponse(id, accessToken, refreshToken);
    }
}

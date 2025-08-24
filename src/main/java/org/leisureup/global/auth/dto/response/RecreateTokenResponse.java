package org.leisureup.global.auth.dto.response;

import org.leisureup.global.auth.dto.*;

public record RecreateTokenResponse(
        String accessToken,
        String refreshToken
) {


    public static RecreateTokenResponse of(Tokens tokens) {
        return new RecreateTokenResponse(
                tokens.accessToken(),
                tokens.refreshToken()
        );
    }
}

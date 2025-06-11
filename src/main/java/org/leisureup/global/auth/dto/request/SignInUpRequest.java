package org.leisureup.global.auth.dto.request;

import jakarta.validation.constraints.*;

public record SignInUpRequest(
        @NotNull AuthType authType,
        @NotNull String token
) {

    public enum AuthType {
        APPLE, GOOGLE, KAKAO
    }
}

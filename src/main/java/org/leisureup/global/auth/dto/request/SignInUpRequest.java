package org.leisureup.global.auth.dto.request;

import jakarta.validation.constraints.*;
import org.leisureup.global.validation.*;

public record SignInUpRequest(
        @ValidEnum(target = AuthType.class, message = "authType 을 매칭할 수 없습니다.")
        AuthType authType,
        @NotBlank(message = "토큰은 비어있을 수 없습니다.")
        String token
) {

    public enum AuthType {
        APPLE, GOOGLE, KAKAO
    }
}

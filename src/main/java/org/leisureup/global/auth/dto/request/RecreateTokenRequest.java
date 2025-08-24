package org.leisureup.global.auth.dto.request;

import jakarta.validation.constraints.*;

public record RecreateTokenRequest(
        @NotBlank(message = "토큰은 비어있을 수 없습니다.")
        String refreshToken
) {

}

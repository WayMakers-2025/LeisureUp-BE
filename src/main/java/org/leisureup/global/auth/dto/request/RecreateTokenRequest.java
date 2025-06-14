package org.leisureup.global.auth.dto.request;

import jakarta.validation.constraints.*;

public record RecreateTokenRequest(
        @NotBlank String refreshToken
) {

}

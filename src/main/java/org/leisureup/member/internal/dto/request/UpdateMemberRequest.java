package org.leisureup.member.internal.dto.request;

import jakarta.validation.constraints.*;

public record UpdateMemberRequest(
        @NotBlank String nickname
) {

}

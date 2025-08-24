package org.leisureup.member.internal.dto.request;

import jakarta.validation.constraints.*;

public record UpdateMemberRequest(
        @NotBlank(message = "nickname 은 비어있을 수 없습니다.")
        String nickname
) {

}

package org.leisureup.member.internal.dto.request;

import jakarta.validation.constraints.*;

public record SavePickLocationRequest(
        @NotNull(message = "locationId 는 비어있을 수 없습니다.")
        @Positive(message = "locationId 는 0 보다 커야합니다.")
        Long locationId
) {

}

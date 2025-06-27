package org.leisureup.member.internal.dto.request;

import jakarta.validation.constraints.*;

public record SavePickLocationRequest(
        @NotNull @Positive
        Long locationId
) {

}

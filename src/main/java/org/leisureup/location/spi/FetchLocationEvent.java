package org.leisureup.location.spi;

import jakarta.validation.constraints.*;

public record FetchLocationEvent(
        @NotNull
        @Positive
        Long locationId
) {

}

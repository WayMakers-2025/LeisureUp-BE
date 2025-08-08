package org.leisureup.location.internal.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum LocationType {
    LEISURE(28),
    HOTEL(32),
    RESTAURANT(39);

    private final long contentTypeId;
}

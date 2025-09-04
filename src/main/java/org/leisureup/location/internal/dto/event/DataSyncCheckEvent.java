package org.leisureup.location.internal.dto.event;

import java.time.*;

public record DataSyncCheckEvent(
        Long locationId,
        LocalDateTime lastModifiedAt,
        LocalDateTime lastChangedAt
) {

}

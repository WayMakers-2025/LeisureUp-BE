package org.leisureup.info.weather.dto.response;

import java.time.*;

public record WarningResponse(
        String content,
        String preWarnedContent,
        LocalDateTime announcedAt,
        LocalDateTime activatedAt,
        long announcementSequence,
        String additionalContent
) {

}

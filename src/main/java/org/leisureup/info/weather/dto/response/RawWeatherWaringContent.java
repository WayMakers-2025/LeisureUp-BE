package org.leisureup.info.weather.dto.response;

import java.time.*;

public record RawWeatherWaringContent(
        String content,
        String preWarnedContent,
        LocalDateTime announcedAt,
        LocalDateTime activatedAt,
        long announcementSequence,
        String additionalContent
) {

}

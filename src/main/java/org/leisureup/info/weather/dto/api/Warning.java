package org.leisureup.info.weather.dto.api;

import com.fasterxml.jackson.annotation.*;
import java.time.*;
import java.time.format.*;

public record Warning(
        @JsonProperty("other") String additionalInfo,
        @JsonProperty("t6") String warningContent,
        @JsonProperty("t7") String preWarningContent,
        @JsonProperty("tmSeq") long sequence,

        @JsonProperty("tmEf")
        @JsonFormat(pattern = "yyyyMMddHHmm")
        LocalDateTime announcedAt,

        // 이건 왜인지 모르겠는데 API 응답이 string 이 아니라 number 임
        // 그래서 @JsonFormat(pattern = "yyyyMMddHHmm") 로는 못함.
        @JsonProperty("tmFc")
        String activeAtRawStr
) {

    public LocalDateTime activeAt() {
        return LocalDateTime.parse(
                activeAtRawStr, DateTimeFormatter.ofPattern("yyyyMMddHHmm")
        );
    }
}

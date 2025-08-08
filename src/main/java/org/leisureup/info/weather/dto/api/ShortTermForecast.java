package org.leisureup.info.weather.dto.api;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShortTermForecast(
        Category category,
        String fcstDate,
        String fcstTime,
        String fcstValue,
        int nx, int ny
) {

    public String getForecastDateInFormat() {

        if (fcstDate == null || fcstDate.length() != 8) {
            throw new IllegalArgumentException(
                    "Cannot format forecast date due to malformed fcstDate");
        }

        return String.format(
                "%s-%s-%s",
                fcstDate.substring(0, 4), fcstDate.substring(4, 6), fcstDate.substring(6, 8)
        );
    }

    @Getter
    @RequiredArgsConstructor
    public enum Category {
        POP("강수확률"),
        PTY("강수형태"),
        PCP("1시간 강수량"),
        REH("습도"),
        SNO("1시간 신적설"),
        SKY("하늘상태"),
        TMP("1시간 기온"),
        TMN("일 최저기온"),
        TMX("일 최고기온"),
        UUU("풍속(동서성분)"),
        VVV("풍속(남북성분)"),
        WAV("파고"),
        VEC("풍향"),
        WSD("풍속");

        private final String desc;
    }
}

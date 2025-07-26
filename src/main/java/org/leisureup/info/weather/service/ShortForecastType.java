package org.leisureup.info.weather.service;

import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.api.ShortTermForecast.*;

@Slf4j
public enum ShortForecastType {

    RAIN_PROBABILITY,           // 강수 확률 (%)
    RAIN_TYPE,                  // 강수 형태 (코드값)
    RAIN_IN_1HOUR,              // 1 시간 강수량 (mm)
    HUMIDITY,                   // 습도 (%)

    SNOW_IN_1HOUR,              // 1 시간 신적설 (cm)

    SKY_CONDITION,              // 하늘 상태 (코드값)

    TEMPERATURE_IN_1HOUR,       // 1 시간 기온 (도)
    MAX_TEMPERATURE_OF_DAY,     // 일 최고기온 (도)
    MIN_TEMPERATURE_OF_DAY,     // 일 최저기온 (도)

    WAVE_HEIGHT,                // 파고 (m)

    WIND_SPEED,                 // 풍속 (m/s)

    OUT_OF_INTEREST             // 관심 밖 나머지
    ;

    public static ShortForecastType resolveType(Category forecastCategory) {
        return switch (forecastCategory) {
            case POP -> ShortForecastType.RAIN_PROBABILITY;
            case PTY -> ShortForecastType.RAIN_TYPE;
            case PCP -> ShortForecastType.RAIN_IN_1HOUR;
            case REH -> ShortForecastType.HUMIDITY;
            case SNO -> ShortForecastType.SNOW_IN_1HOUR;
            case SKY -> ShortForecastType.SKY_CONDITION;
            case TMP -> ShortForecastType.TEMPERATURE_IN_1HOUR;
            case TMN -> ShortForecastType.MAX_TEMPERATURE_OF_DAY;
            case TMX -> ShortForecastType.MIN_TEMPERATURE_OF_DAY;
            case WAV -> ShortForecastType.WAVE_HEIGHT;
            case WSD -> ShortForecastType.WIND_SPEED;
            case null -> throw new IllegalArgumentException("Forecast category is null");
            default -> ShortForecastType.OUT_OF_INTEREST;
        };
    }
}

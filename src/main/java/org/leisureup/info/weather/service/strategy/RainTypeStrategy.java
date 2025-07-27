package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 강수 종류 정보를 넣어주는 전략
 */
@Component
public class RainTypeStrategy extends BaseStrategy {

    private static String resolveRainType(String forecastValue) {
        return switch (forecastValue) {
            case "0" -> "강수 없음";
            case "1" -> "비";
            case "2" -> "비/눈";
            case "3" -> "눈";
            case "4" -> "소나기";
            default -> "정보 없음";
        };
    }

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String resolvedInfo = resolveRainType(forecastInfo.fcstValue());
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), resolvedInfo);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.RAIN_TYPE;
    }
}

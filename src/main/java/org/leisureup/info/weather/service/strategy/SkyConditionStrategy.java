package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 하늘 상태 정보를 넣어주는 전략
 */
@Component
public class SkyConditionStrategy extends BaseStrategy {

    private static String resolveSkyCondition(String forecastValue) {
        return switch (forecastValue) {
            case "1" -> "맑음";
            case "3" -> "구름많음";
            case "4" -> "흐림";
            default -> "정보 없음";
        };
    }

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String resolvedSkyCondition = resolveSkyCondition(forecastInfo.fcstValue());
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), resolvedSkyCondition);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.SKY_CONDITION;
    }
}

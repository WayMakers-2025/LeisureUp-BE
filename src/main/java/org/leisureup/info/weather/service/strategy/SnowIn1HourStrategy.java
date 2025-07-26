package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 1 시간 동안 쌓이는 눈 정보를 넣어주는 전략
 */
@Component
public class SnowIn1HourStrategy extends BaseStrategy {

    private static String resolveSnowAmount(String forecastValue) {

        if (
                forecastValue == null ||
                "-".equals(forecastValue) ||
                forecastValue.startsWith("-") ||
                "0".equals(forecastValue) ||
                "적설없음".equals(forecastValue)
        ) {
            return "적설 없음";
        }

        double value = Double.parseDouble(forecastValue);

        if (0.1 <= value && value < 0.5) {
            return "0.5cm 미만";
        }

        if (5.0 <= value) {
            return "5.0cm 이상";
        }

        return String.format("%.1fcm", value);
    }

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String resolvedSnowAmount = resolveSnowAmount(forecastInfo.fcstValue());
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), resolvedSnowAmount);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.SNOW_IN_1HOUR;
    }
}

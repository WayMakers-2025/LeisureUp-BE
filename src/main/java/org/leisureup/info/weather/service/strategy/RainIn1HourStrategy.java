package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 1 시간 강수량 정보를 넣어주는 전략
 */
@Component
public class RainIn1HourStrategy extends BaseStrategy {

    private static String resolveRainAmount(String forecastValue) {

        if (
                forecastValue == null ||
                "-".equals(forecastValue) ||
                forecastValue.startsWith("-") ||
                "0".equals(forecastValue) ||
                "강수없음".equals(forecastValue)
        ) {
            return "강수 없음";
        }

        if (
                forecastValue.endsWith("미만") ||
                forecastValue.endsWith("이상") ||
                forecastValue.contains("~")
        ) {
            return forecastValue;
        }

        forecastValue = forecastValue.replace("mm", "");
        double value = Double.parseDouble(forecastValue);

        if (0.1 <= value && value < 1.0) {
            return "1mm 미만";
        }

        if (30.0 <= value && value < 50.0) {
            return "30.0 ~ 50.0mm";
        }

        if (50.0 <= value) {
            return "50.0mm 이상";
        }

        return String.format("%.1fmm", value);
    }

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String rainAmount = resolveRainAmount(forecastInfo.fcstValue());
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), rainAmount);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.RAIN_IN_1HOUR;
    }
}

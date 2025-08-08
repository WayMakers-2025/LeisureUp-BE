package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 기온 정보를 넣어주는 전략
 */
@Component
public class TemperatureIn1HourStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String temperature = super.formatDataIfPossible(
                forecastInfo, "%sC"
        );
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), temperature);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.TEMPERATURE_IN_1HOUR;
    }
}

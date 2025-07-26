package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 풍속 정보를 넣어주는 전략
 */
@Component
public class WindSpeedStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String windSpeed = super.formatDataIfPossible(forecastInfo, "%sm/s");
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), windSpeed);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.WIND_SPEED;
    }
}

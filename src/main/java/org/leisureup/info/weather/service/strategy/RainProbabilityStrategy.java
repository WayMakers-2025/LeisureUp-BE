package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

@Component
public class RainProbabilityStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String rainProbability = super.formatDataIfPossible(forecastInfo, "%s%%");
        String forecastTime = forecastInfo.fcstTime();

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), rainProbability);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.RAIN_PROBABILITY;
    }
}

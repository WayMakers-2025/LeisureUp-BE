package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 일일 최저 기온 정보를 넣어주는 전략
 */
@Component
public class MinTemperatureOfDayStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        if (!super.isDataEmpty(forecastInfo)) {
            dst.setMinTemp(Double.parseDouble(forecastInfo.fcstValue()));
        }
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.MIN_TEMPERATURE_OF_DAY;
    }
}

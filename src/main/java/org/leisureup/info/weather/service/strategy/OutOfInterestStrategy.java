package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.api.ShortTermForecast.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 관심밖 정보들 처리하는 전략
 *
 * @see ShortForecastType#resolveType(Category)
 */
@Component
public class OutOfInterestStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        // 관심 밖이므로 정보 안넣음.
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.OUT_OF_INTEREST;
    }
}

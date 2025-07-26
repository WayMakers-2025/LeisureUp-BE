package org.leisureup.info.weather.service;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;

public interface ShortForecastStrategy {

    void addForecastInfo(
            ShortTermForecast forecastInfo,
            ShortForecastInDayDto dst
    );

    ShortForecastType getType();
}

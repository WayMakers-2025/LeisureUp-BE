package org.leisureup.info.weather.dto.response;

import java.util.*;

public record ShotTermForecastResponse(
        String forecastDate,
        List<SingleShortTermForecast> data
) {

}

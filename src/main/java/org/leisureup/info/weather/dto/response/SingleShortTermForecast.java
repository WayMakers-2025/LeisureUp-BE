package org.leisureup.info.weather.dto.response;

public record SingleShortTermForecast(
        String time,
        String rainProbability,
        String rainType,
        String rainAmount,
        String humidity,
        String skyCondition,
        String temperature,
        String waveHeight,
        String windSpeed
) {

}

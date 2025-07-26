package org.leisureup.info.weather.dto;

import static org.leisureup.info.weather.service.ShortForecastType.*;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.service.*;

@Slf4j
@Getter
public class ShortForecastInTimeDto {

    private final String forecastTime;
    private final Map<ShortForecastType, String> forecasts;

    public ShortForecastInTimeDto(String forecastTime) {
        this.forecastTime = forecastTime;
        forecasts = new HashMap<>();
    }

    public void addForecast(ShortForecastType key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Forecast key cannot be null");
        }

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Forecast value cannot be null or empty.");
        }

        if (forecasts.containsKey(key)) {
            throw new IllegalArgumentException("Forecast key already exists.");
        }

        forecasts.put(key, value);
    }

    public SingleShortTermForecast toRecord() {
        return new SingleShortTermForecast(
                forecastTime,
                supplyValue(RAIN_PROBABILITY), supplyValue(RAIN_TYPE),
                supplyValue(RAIN_IN_1HOUR), supplyValue(HUMIDITY),
                supplyValue(SKY_CONDITION), supplyValue(TEMPERATURE_IN_1HOUR),
                supplyValue(WAVE_HEIGHT), supplyValue(WIND_SPEED)
        );
    }

    private String supplyValue(ShortForecastType key) {
        return forecasts.getOrDefault(key, "정보 없음");
    }
}

package org.leisureup.info.weather.dto;

import java.util.*;
import lombok.*;
import org.leisureup.info.weather.dto.response.*;

@ToString
public class ShortForecastInDayDto {

    @Getter
    private final String forecastDate;
    private final Map<String, ShortForecastInTimeDto> forecastOnTime;

    @Setter
    private Double maxTemp, minTemp;

    public ShortForecastInDayDto(String forecastDate) {
        this.forecastDate = forecastDate;
        forecastOnTime = new HashMap<>();
    }

    public ShortForecastInTimeDto getOrCreateForecastInTime(String forecastTime) {
        if (forecastTime == null) {
            throw new IllegalArgumentException("Forecast time cannot be null");
        }

        if (!forecastOnTime.containsKey(forecastTime)) {
            forecastOnTime.put(forecastTime, new ShortForecastInTimeDto(forecastTime));
        }

        return forecastOnTime.get(forecastTime);
    }

    public ShotTermForecastResponse toResponse() {

        List<SingleShortTermForecast> data = forecastOnTime.values().stream()
                .map(ShortForecastInTimeDto::toRecord)
                .sorted(Comparator.comparing(SingleShortTermForecast::time))
                .toList();

        return new ShotTermForecastResponse(this.forecastDate, data);
    }
}

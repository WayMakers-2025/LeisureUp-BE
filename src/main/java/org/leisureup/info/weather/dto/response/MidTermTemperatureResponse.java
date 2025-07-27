package org.leisureup.info.weather.dto.response;

import java.time.*;
import java.util.*;

public record MidTermTemperatureResponse(
        Map<LocalDate, TemperatureForecastInfo> temperatureForecasts
) {

    public record TemperatureForecastInfo(
            int minTemperature, int maxTemperature
    ) {

        public static TemperatureForecastInfo of(int min, int max) {
            return new TemperatureForecastInfo(min, max);
        }
    }
}

package org.leisureup.info.weather.dto.response;

import java.time.*;
import java.util.*;

public record MidTermLandResponse(
        Map<LocalDate, List<LandForecastInfo>> landForecasts
) {

    public enum Type {
        AM, PM, WHOLE_DAY
    }

    public record LandForecastInfo(
            Type type, int rainProbability, String weatherInspection
    ) {

        public static LandForecastInfo of(Type type, int prob, String inspect) {
            return new LandForecastInfo(type, prob, inspect);
        }
    }
}

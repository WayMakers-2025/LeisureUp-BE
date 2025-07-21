package org.leisureup.info.weather.dto;

import java.util.*;

public record SingleWeatherWarning(
        String warningType,
        List<String> recommendations,
        List<String> warnedRegions
) {

    public static SingleWeatherWarning of(
            String warningType,
            List<String> recommendations,
            List<String> warnedRegions
    ) {
        return new SingleWeatherWarning(
                warningType, recommendations, warnedRegions
        );
    }
}

package org.leisureup.info.weather.dto;

import java.io.*;
import java.util.*;

public record SingleWeatherWarning(
        String warningType,
        List<String> recommendations,
        List<String> warnedRegions
) implements Serializable {

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

package org.leisureup.info.weather.dto.api;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TemperatureMidForecast(
        String regId,
        int taMin4, int taMax4,
        int taMin5, int taMax5,
        int taMin6, int taMax6,
        int taMin7, int taMax7,
        int taMin8, int taMax8,
        int taMin9, int taMax9,
        int taMin10, int taMax10
) {

}

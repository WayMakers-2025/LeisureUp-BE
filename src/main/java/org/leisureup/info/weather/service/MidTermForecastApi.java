package org.leisureup.info.weather.service;

import org.leisureup.global.response.external.*;
import org.leisureup.global.response.external.weather.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(
        name = "WeatherMidTermForecastApi",
        url = "${feign.weather.forecast}",
        configuration = DefaultFeignErrorDecoder.class
)
public interface MidTermForecastApi {

    @GetMapping("/getMidLandFcst")
    WeatherApiResponse<LandMidForecast> getLandForecast(
            @RequestParam String serviceKey,
            @RequestParam("dataType") String respType,
            @RequestParam("regId") String regionId,
            @RequestParam("tmFc") String forecastAt,
            @RequestParam int numOfRows
    );

    @GetMapping("/getMidTa")
    WeatherApiResponse<TemperatureMidForecast> getTemperatureForecast(
            @RequestParam String serviceKey,
            @RequestParam("dataType") String respType,
            @RequestParam("regId") String regionId,
            @RequestParam("tmFc") String forecastAt,
            @RequestParam int numOfRows
    );
}

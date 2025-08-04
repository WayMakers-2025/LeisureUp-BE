package org.leisureup.info.weather.service.client;

import org.leisureup.global.response.external.weather.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(
        name = "ShortTermForecastApi",
        url = "${feign.weather.forecast.short-term}",
        configuration = WeatherApiConfig.class
)
public interface ShortTermForecastApi {

    @GetMapping("/getVilageFcst")
    WeatherApiResponse<ShortTermForecast> getShortTermForecast(
            @RequestParam String serviceKey,
            @RequestParam("dataType") String respType,
            @RequestParam("base_date") String baseDate,
            @RequestParam("base_time") String baseTime,
            @RequestParam int nx,
            @RequestParam int ny,
            @RequestParam int pageNo,
            @RequestParam int numOfRows
    );

}

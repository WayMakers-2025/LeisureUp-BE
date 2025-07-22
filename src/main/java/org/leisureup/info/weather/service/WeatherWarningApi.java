package org.leisureup.info.weather.service;

import org.leisureup.global.response.external.*;
import org.leisureup.global.response.external.weather.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(
        name = "WeatherWarningApi",
        url = "${feign.weather.warning}",
        configuration = DefaultFeignErrorDecoder.class
)
public interface WeatherWarningApi {

    @GetMapping("/getPwnStatus")
    WeatherApiResponse<Warning> getWeatherWarning(
            @RequestParam("serviceKey") String key,
            @RequestParam("dataType") String rspType
    );
}

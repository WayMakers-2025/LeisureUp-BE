package org.leisureup.info.weather.controller;

import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.service.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weathers")
public class WeatherController {

    private final WeatherInformService weatherInformService;

    @GetMapping("/warning")     // 기상 특보 현황 조회
    public ApiResponse<WeatherWarningResponse> getWeatherWarning() {

        var resp = weatherInformService.getWeatherWarning();

        return ApiResponse.success(200, resp);
    }

}

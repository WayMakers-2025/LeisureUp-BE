package org.leisureup.info.weather.controller;

import java.util.*;
import lombok.*;
import org.leisureup.global.response.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.service.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

@Validated
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

    /**
     * 중기 육상 예보를 조회 (강수량 & 기상 예측 포함)
     *
     * @param x 경도 {@code (126.98161)}
     * @param y 위도 {@code (37.568477)}
     */
    @GetMapping("/forecasts/midterm/land")
    public ApiResponse<MidTermLandResponse> getMidTermLandForecast(
            @RequestParam double x, @RequestParam double y
    ) {

        var resp = weatherInformService.getMidTermLandForecast(x, y);

        return ApiResponse.success(200, resp);
    }

    /**
     * 중기 기온 에보를 조회
     * @param x 경도 {@code (126.98161)}
     * @param y 위도 {@code (37.568477)}
     */
    @GetMapping("/forecasts/midterm/temperature")
    public ApiResponse<MidTermTemperatureResponse> getMidTermTemperatureForecast(
            @RequestParam double x, @RequestParam double y
    ) {

        var resp = weatherInformService.getMidTermTemperatureForecast(x, y);

        return ApiResponse.success(200, resp);
    }

    /**
     * 단기 예보를 조회
     *
     * @param x 경도 {@code (126.98161)}
     * @param y 위도 {@code (37.568477)}
     */
    @GetMapping("/forecasts/short-term")
    public ApiResponse<List<ShotTermForecastResponse>> getShortTermForecasts(
            @RequestParam double x, @RequestParam double y
    ) {

        var resp = weatherInformService.getShortTermForecast(x, y);

        return ApiResponse.success(200, resp);
    }
}

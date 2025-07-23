package org.leisureup.info.weather.service;

import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class WeatherWarningApiClient {

    private final String key, rspType;
    private final WeatherWarningApi weatherWarningApi;

    public WeatherWarningApiClient(
            WeatherWarningApi weatherWarningApi,
            @Value("${weatherApi.warning.key}") String key,
            @Value("${weatherApi.warning.type}") String rspType
    ) {
        this.key = key;
        this.rspType = rspType;
        this.weatherWarningApi = weatherWarningApi;
    }

    public Warning getWeatherWarning() {
        var resp = weatherWarningApi.getWeatherWarning(key, rspType, 1);

        if (resp == null || !resp.isSuccess()) {
            throw new WeatherWarningApiException("API 통신 중 문제가 발생했습니다.");
        }

        if (resp.isEmpty()) {
            throw new WeatherWarningApiException("API 통신에는 성공했으나 content 가 비어있습니다.");
        }

        return resp.getSingleItem();
    }
}


package org.leisureup.travel.internal.travel.service;

import lombok.RequiredArgsConstructor;
import org.leisureup.travel.internal.travel.dto.TemperatureApiResponse;
import org.leisureup.travel.internal.travel.dto.WeatherApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TemperatureService {
    private final Temperature temperature;

    @Value("${weatherApi.warning.key}")
    private String key;

    public TemperatureApiResponse getTemperature(String regId, String tmFc) {
        return temperature.getTemperature(key, 10, 1, "JSON", regId, tmFc);
    }

    public WeatherApiResponse getWeatherDetail(String regId, String tmFc){
        return temperature.getWeatherDetail(key, 10, 1, "JSON", regId, tmFc);
    }
}

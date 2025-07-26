package org.leisureup.info.weather.service;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
public class ShortTermForecastComposer {

    private final Map<ShortForecastType, ShortForecastStrategy> strategyMap;

    public ShortTermForecastComposer(List<ShortForecastStrategy> strategyList) {
        this.strategyMap = strategyList.stream().collect(
                Collectors.toMap(ShortForecastStrategy::getType, Function.identity())
        );
    }

    private static List<ShortTermForecast> filterInterests(List<ShortTermForecast> forecasts) {
        return forecasts.stream()
                .filter(
                        f ->
                                ShortForecastType.resolveType(f.category()) !=
                                ShortForecastType.OUT_OF_INTEREST
                )
                .toList();
    }

    public List<ShortForecastInDayDto> composeForecasts(
            List<ShortTermForecast> forecasts
    ) {

        // 관심 있는 예보 정보만 솎아낸다.
        List<ShortTermForecast> targets = filterInterests(forecasts);

        // 예보 날짜별로 묶는다.
        Map<String, List<ShortTermForecast>> forecastsMap = targets.stream().collect(
                Collectors.groupingBy(ShortTermForecast::getForecastDateInFormat)
        );

        // 예보 날짜별 정보를 담을 DTO 를 만든다.
        List<ShortForecastInDayDto> infos = forecastsMap.keySet().stream()
                .map(ShortForecastInDayDto::new)
                .toList();

        // 예보 날짜별 정보를 담는다.
        for (ShortForecastInDayDto info : infos) {

            // 어느 날짜에 속한 예보 정보들을
            var forecastsInOneDay = forecastsMap.get(info.getForecastDate());

            // 타입을 식별해 정보를 넣어준다.
            for (ShortTermForecast forecastOnTime : forecastsInOneDay) {

                ShortForecastType type = ShortForecastType.resolveType(
                        forecastOnTime.category()
                );

                strategyMap.get(type).addForecastInfo(forecastOnTime, info);
            }
        }

        return infos;
    }
}

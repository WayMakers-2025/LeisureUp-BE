package org.leisureup.info.weather.dto.response;

import java.util.*;

/**
 * 단기 예보 응답으로 제공할 dto
 *
 * @param forecastDate   예보 날짜
 * @param maxTemperature 해당 날짜의 최고 기온 {@code (정보가 없으면 null)}
 * @param minTemperature 해당 날짜의 최저 기온 {@code (정보가 없으면 null)}
 * @param data           날짜에 속한 예보 정보들 (1 시간 간격)
 */
public record ShotTermForecastResponse(
        String forecastDate,
        Double maxTemperature,
        Double minTemperature,
        List<SingleShortTermForecast> data
) {

}

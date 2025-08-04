package org.leisureup.info.weather.dto.response;

/**
 * 특정 시각의 단기 예보 정보
 *
 * @param time            예보 시각
 * @param rainProbability 강수 확률
 * @param rainType        강수 형태 (비, 눈, 비/눈 등)
 * @param rainAmount      강수량
 * @param humidity        습도
 * @param skyCondition    하늘 상태 (맑음, 흐림 등)
 * @param temperature     기온
 * @param waveHeight      파도 높이
 * @param windSpeed       풍속
 */
public record SingleShortTermForecast(
        String time,
        String rainProbability,
        String rainType,
        String rainAmount,
        String humidity,
        String skyCondition,
        String temperature,
        String waveHeight,
        String windSpeed
) {

}
